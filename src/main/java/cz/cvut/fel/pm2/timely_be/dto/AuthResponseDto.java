package cz.cvut.fel.pm2.timely_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
    private EmployeeDto employee;
}