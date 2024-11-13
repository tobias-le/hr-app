package cz.cvut.fel.pm2.timely_be.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "teams")
@Data
public class Team {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "employeeId")
    private Employee manager;

    @OneToMany(mappedBy = "team")  // `mappedBy` points to `team` in Employee
    private List<Employee> members;

    @Column(nullable = false)
    private boolean deleted = false;

    @NonNull
    public List<Employee> getMembers() {
        if (members == null) {
            members = new ArrayList<>();
        }
        return members;
    }
}
