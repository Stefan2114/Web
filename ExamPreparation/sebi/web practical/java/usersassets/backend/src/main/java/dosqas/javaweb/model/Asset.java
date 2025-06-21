package dosqas.javaweb.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="Assets")
public class Asset {
    @Id
    // makes it auto increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer value;
}