package cz.cvut.fel.pm2.timely_be.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ProjectDto {
    private Long projectId;
    private String name;
    private String managerName;
    private Long managerId;
    private Set<EmployeeDto> members;
}
