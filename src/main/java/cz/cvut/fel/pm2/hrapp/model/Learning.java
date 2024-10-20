package cz.cvut.fel.pm2.hrapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
public class Learning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long learningId;

//    @ManyToOne
//    @JoinColumn(name = "employeeId", nullable = false)
//    private Employee employee;

    private String title;

    private Date assignmentDate;
    private Date dueDate;
}
