package cz.cvut.fel.pm2.timely_be.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "projects")
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long projectId;
    private String name;
}
