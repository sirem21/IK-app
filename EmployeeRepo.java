package com.manager.employeemanager.repo;

import com.manager.employeemanager.model.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmployeeRepo extends JpaRepository<Employee,Long> {
   @Transactional

    void deleteEmployeeById(Long id);
    Optional<Employee> findEmployeeById(Long id);

}
