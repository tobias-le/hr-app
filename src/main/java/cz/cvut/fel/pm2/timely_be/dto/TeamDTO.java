package cz.cvut.fel.pm2.timely_be.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeamDTO {
    private Long teamId;
    private String name;
    private String manager;
    private List<String> members;
}
