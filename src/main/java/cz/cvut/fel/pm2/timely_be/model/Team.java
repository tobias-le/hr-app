package cz.cvut.fel.pm2.timely_be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "teams")
@Table(name = "teams", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"}, name = "uk_team_name")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "employeeId")
    private Employee manager;

    @OneToMany(mappedBy = "team")
    private Set<Employee> members = new HashSet<>();

    @Column(nullable = false)
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "parent_team_id")
    private Team parentTeam;

    public void addMember(Employee employee) {
        if (this.members == null) {
            this.members = new HashSet<>();
        }
        this.members.add(employee);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        if (!Objects.equals(id, team.id)) return false;
        return Objects.equals(name, team.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
