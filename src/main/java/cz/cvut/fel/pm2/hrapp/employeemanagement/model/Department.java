package cz.cvut.fel.pm2.hrapp.employeemanagement.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    @Column(nullable = false)
    private String departmentName;
}