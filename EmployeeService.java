package com.manager.employeemanager.service;

import com.manager.employeemanager.exception.UserNotFoundException;
import com.manager.employeemanager.model.Employee;
import com.manager.employeemanager.repo.EmployeeRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class EmployeeService {
    private final EmployeeRepo employeeRepo;

    @Autowired
    public EmployeeService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    //create the unique id for an employee
    public Employee addEmployee(Employee employee) throws Exception{
        checkEmployee(employee);
       employee.setEmployeeCode(UUID.randomUUID().toString());
       return employeeRepo.save(employee);
    }

    private void checkEmployee(Employee employee) throws Exception{
        if (employee.getName().equals("") ){
            throw new Exception("Please add the name of employee");
        }
        if (employee.getEmail().equals("")){
            throw new Exception("Please add the mail of employee");
        }
        if (employee.getJobTitle().equals("")){
            throw new Exception("Please add the job title of employee");
        }
        if (employee.getPhone().equals("")){
            throw new Exception("Please add the phone of employee");
        }
        if(employee.getImageUrl().equals("")){
            throw new Exception("Please add the image url of employee");
        }
        //throw exception if that employee is already exist by checkng the email
        if (isUserExists(employee.getEmail(),employee.getPhone())) {
            throw new Exception("This employee is already exist!");
        }
        //throw exception if number do not have 11 digits or contain letters or symbols
        if (containsLettersOrSymbols(employee.getPhone()) || (employee.getPhone().length() != 11)) {
            throw new Exception("Invalid phone number");
        }
        //throw exception if mail does not contain "@" symbol
         if(!employee.getEmail().contains("@")){
            throw new Exception("Invalid mail address");
        }
    }
    public static boolean containsLettersOrSymbols(String phoneNumber) {
        // Regular expression to match any non-digit characters
        Pattern pattern = Pattern.compile("[^0-9]");
        return pattern.matcher(phoneNumber).find();
    }
    public boolean isUserExists(String email, String phone) {
        for(Employee emp : allEmployees()){
            if (emp.getEmail().equals(email) || emp.getPhone().equals(phone)){
                return true;
            }
        }
        return false;
    }
    public boolean isUserExistsExceptItself(Employee employee) {
        for(Employee emp : allEmployees() ){
            if(employee.getId().equals(emp.getId())){
                continue;
            }
            if(employee.getPhone().equals(emp.getPhone()) || employee.getEmail().equals(emp.getEmail())){
                return true;
            }
        }
        return false;
    }
    //list all employees
    public List<Employee> allEmployees(){
        return employeeRepo.findAll();
    }

    //find employee by Id
    public Employee findEmployeeById(Long id) throws Exception {
        isDBEmpty();
        return employeeRepo.findEmployeeById(id).orElseThrow(() -> new UserNotFoundException("User with id "+id+"  was not found!"));
    }

    //update employee information
    public Employee updateEmployee(Employee employee) throws Exception{
        if(isUserExistsExceptItself(employee)) {
            throw new Exception("The user is already exist!");
        }
        if (containsLettersOrSymbols(employee.getPhone()) || (employee.getPhone().length() != 11)) {
            throw new Exception("Invalid phone number");
        }
        if(!employee.getEmail().contains("@")){
            throw new Exception("Invalid mail address");
        }
            return employeeRepo.save(employee);


    }
    //check if the databases is empty or not
    private  void isDBEmpty() throws Exception{
        if(allEmployees().isEmpty()){
            throw new Exception("The list is empty");
        }
    }
    @Transactional
    //delete employee by id
    public void deleteEmployee(Long id) throws Exception{
            isDBEmpty();
            employeeRepo.deleteEmployeeById(id);
    }


}
