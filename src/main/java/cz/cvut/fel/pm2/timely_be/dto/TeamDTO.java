package cz.cvut.fel.pm2.timely_be.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeamDTO {
    private Long teamId;
    private String name;
    private String managerName;
    private Long managerId;
    private List<EmployeeDto> members;
}
