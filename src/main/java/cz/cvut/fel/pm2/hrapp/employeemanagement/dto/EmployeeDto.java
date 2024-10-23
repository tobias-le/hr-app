package cz.cvut.fel.pm2.hrapp.employeemanagement.dto;

import cz.cvut.fel.pm2.hrapp.employeemanagement.model.enums.ContractType;
import cz.cvut.fel.pm2.hrapp.employeemanagement.model.enums.WorkPercentage;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmployeeDto {
    private String departmentName;
    private String name;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private String contractType; // FULL_TIME,PART_TIME,CONTRACTOR
    private Integer contractualHours; // Total hours per week
    private WorkPercentage workPercentage; // Percentage of full-time work
    private String accountNumber; //    FULL,HALF,PART
    private String supervisorName;
    // The total amount of leave/vacation days the employee has available
    private BigDecimal availableTimeOff;
}