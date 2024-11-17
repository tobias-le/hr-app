package cz.cvut.fel.pm2.timely_be.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.cvut.fel.pm2.timely_be.repository.composite.EmployeeLearningId;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "employee_learnings")
@Data
public class EmployeeLearning {

    @EmbeddedId
    private EmployeeLearningId id;

    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnore
    private Employee employee;

    @ManyToOne
    @MapsId("learningId")
    @JoinColumn(name = "learning_id", nullable = false)
    @JsonIgnore
    private Learning learning;

    @Column(name = "date")
    private LocalDate date;
}

