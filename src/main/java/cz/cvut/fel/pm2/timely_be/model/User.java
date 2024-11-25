package cz.cvut.fel.pm2.timely_be.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
} 