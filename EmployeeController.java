package com.manager.employeemanager.controller;

import com.manager.employeemanager.model.Employee;
import com.manager.employeemanager.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


//this class will represent controller layer
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService) {

        this.employeeService = employeeService;
    }

    @GetMapping("/all")
    //  ResponseEntity<> return the HTTP response(including status code, headers, body)
    public ResponseEntity<List<Employee>> getAllEmployees(){
        try{
            List<Employee> employees = employeeService.allEmployees();
            if(employees.isEmpty()){
                System.out.println("The list is empty");
            }
            return  new ResponseEntity<>(employees, HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println("Exception occurred while deleting employee: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id){
        try{
            Employee employee = employeeService.findEmployeeById(id);
            System.out.println("The employee was successfully found!");
            return  new ResponseEntity<>(employee, HttpStatus.OK);
        }
         catch (Exception e){
             System.out.println("Exception occured while deleting employee: "+ e.getMessage());
             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/added")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee){
        try{
            Employee addedEmployee = employeeService.addEmployee(employee);
            System.out.println("The employee was successfully added!");
            return  new ResponseEntity<>(addedEmployee, HttpStatus.CREATED);
        }
        catch (Exception e){
            System.out.println("Exception occurred while adding employee: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/updated")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee){
        try{
            Employee updatedEmployee = employeeService.updateEmployee(employee);
            return  new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        }
        catch (Exception e){
            if(e.getMessage().equals("Invalid phone number")){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if(e.getMessage().equals("Invalid mail address")){
                return new ResponseEntity<>(HttpStatus.valueOf(402));
            }
            else{
                System.out.println("Exception occurred while updating employee: " + e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
    @DeleteMapping("/deleted/{id}")
    public ResponseEntity<?> deletedEmployee(@PathVariable("id") Long id) {
        try {
            employeeService.deleteEmployee(id);
            System.out.println("The employee was successfully deleted!");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e) {
            System.out.println("Exception occurred while deleting employee: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
