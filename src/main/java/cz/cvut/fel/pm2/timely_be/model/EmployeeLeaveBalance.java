package cz.cvut.fel.pm2.timely_be.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data           //chybela anotace, vracelo prázdný objekt
public class EmployeeLeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long employeeId;    //zde byla reference na employee, takže bych musel vyhledávat employee a pak setnout a ne pouze vkládat id
    private Integer vacationDaysLeft;
    private Integer sickDaysLeft;
    private Integer personalDaysLeft;
}
