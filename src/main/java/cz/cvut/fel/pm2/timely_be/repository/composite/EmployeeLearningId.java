package cz.cvut.fel.pm2.timely_be.repository.composite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class EmployeeLearningId {

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "learning_id")
    private Long learningId;

}
