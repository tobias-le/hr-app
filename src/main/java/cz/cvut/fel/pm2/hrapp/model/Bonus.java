package cz.cvut.fel.pm2.hrapp.model;

import cz.cvut.fel.pm2.hrapp.model.enums.Currency;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Bonus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bonusId;

//    @ManyToOne
//    @JoinColumn(name = "employeeId", nullable = false)
//    private Employee employee;

    @Column(nullable = false)
    private BigDecimal amount; // Flat monetary value

    @Enumerated(EnumType.STRING)
    private Currency currency; // Enum for currency
}