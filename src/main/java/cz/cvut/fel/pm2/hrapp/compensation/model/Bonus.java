package cz.cvut.fel.pm2.hrapp.compensation.model;

import cz.cvut.fel.pm2.hrapp.compensation.model.enums.Currency;
import cz.cvut.fel.pm2.hrapp.employeemanagement.model.Employee;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Bonus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bonusId;

    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private BigDecimal amount; // Flat monetary value

    @Enumerated(EnumType.STRING)
    private Currency currency; // Enum for currency
}