package cz.cvut.fel.pm2.timely_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeNameWithIdDto {
    private Long id;
    private String name;
}
