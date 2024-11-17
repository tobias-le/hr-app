package cz.cvut.fel.pm2.timely_be.model;

import cz.cvut.fel.pm2.timely_be.enums.EmploymentType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "employees")
@Data
@ToString(exclude = {"currentProjects", "team"})
public class Employee {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long employeeId;

    private String name;

    private String jobTitle;

    @Enumerated(STRING)
    private EmploymentType employmentType;

    private String email;

    private String phoneNumber;

    private Integer annualSalary;
    private Integer annualLearningBudget;
    private Integer annualBusinessPerformanceBonusMax;
    private Integer annualPersonalPerformanceBonusMax;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Project> currentProjects;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(nullable = false)
    private boolean deleted = false;

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }

        if (!phoneNumber.matches("\\d{9}")) {
            throw new IllegalArgumentException("Phone number must be 9 digits long");
        }
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        if (!Objects.equals(employeeId, employee.employeeId)) return false;
        if (!Objects.equals(name, employee.name)) return false;
        if (!Objects.equals(jobTitle, employee.jobTitle)) return false;
        if (employmentType != employee.employmentType) return false;
        if (!Objects.equals(email, employee.email)) return false;
        return Objects.equals(phoneNumber, employee.phoneNumber);
    }

    @Override
    public int hashCode() {
        int result = employeeId != null ? employeeId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (jobTitle != null ? jobTitle.hashCode() : 0);
        result = 31 * result + (employmentType != null ? employmentType.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        return result;
    }
}
