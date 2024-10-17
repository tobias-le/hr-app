package cz.cvut.fel.pm2.hrapp.employeemanagement.rest.dto;

import cz.cvut.fel.pm2.hrapp.employeemanagement.model.Address;
import cz.cvut.fel.pm2.hrapp.employeemanagement.model.enums.ContractType;
import cz.cvut.fel.pm2.hrapp.employeemanagement.model.enums.WorkPercentage;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmployeeDto {

    private Long departmentId;
    private String name;
    private Address address;
    private ContractType contractType;
    private Integer contractualHours;
    private WorkPercentage workPercentage;
    private String accountNumber;
    private Long supervisorId;
    private BigDecimal availableTimeOff;
}
