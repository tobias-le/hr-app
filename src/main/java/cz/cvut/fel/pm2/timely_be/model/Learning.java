package cz.cvut.fel.pm2.timely_be.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "learnings")
@Data
public class Learning {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long learningId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String link;

}
