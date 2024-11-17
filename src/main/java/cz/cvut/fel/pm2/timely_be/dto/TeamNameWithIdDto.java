package cz.cvut.fel.pm2.timely_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamNameWithIdDto {
    private Long teamId;
    private String name;
} 