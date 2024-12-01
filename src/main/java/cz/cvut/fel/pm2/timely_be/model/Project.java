package cz.cvut.fel.pm2.timely_be.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "projects")
@Table(name = "projects", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"}, name = "uk_project_name")
})
@Data
@ToString(exclude = {"members", "manager"})
public class Project {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long projectId;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", referencedColumnName = "employeeId")
    private Employee manager;

    @ManyToMany(mappedBy = "currentProjects", fetch = FetchType.LAZY)
    private Set<Employee> members;

    @Column(nullable = false)
    private boolean deleted = false;
}
