package cz.cvut.fel.pm2.timely_be.model;

import cz.cvut.fel.pm2.timely_be.enums.EmploymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "employees")
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long employeeId;

    private String name;

    private String jobTitle;

    @Enumerated(STRING)
    private EmploymentStatus employmentStatus;

    private String email;

    private String phoneNumber;

    @ManyToMany
    private List<Project> currentProjects;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
