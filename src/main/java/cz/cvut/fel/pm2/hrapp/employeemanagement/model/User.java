package cz.cvut.fel.pm2.hrapp.employeemanagement.model;

import cz.cvut.fel.pm2.hrapp.employeemanagement.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role; // Enum for roles
}
