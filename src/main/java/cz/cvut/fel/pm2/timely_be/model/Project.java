package cz.cvut.fel.pm2.timely_be.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "projects")
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long projectId;

    private String name;

    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "employeeId")
    private Employee manager;

    @ManyToMany(mappedBy = "currentProjects")
    private List<Employee> members;
}
