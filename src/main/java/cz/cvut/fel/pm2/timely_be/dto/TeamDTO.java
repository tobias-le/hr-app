package cz.cvut.fel.pm2.timely_be.dto;

import lombok.Data;

import java.util.Set;

@Data
public class TeamDTO {
    private Long teamId;
    private String name;
    private String managerName;
    private Long managerId;
    private String managerJobTitle;
    private Set<EmployeeDto> members;
    private TeamDTO parentTeam;
}
