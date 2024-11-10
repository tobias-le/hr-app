package cz.cvut.fel.pm2.timely_be.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeDto {
    private Long id;
    private String name;
    private String jobTitle;
    private String employmentStatus;
    private String email;
    private String phoneNumber;
    private List<String> currentProjects;
}
