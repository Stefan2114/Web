package dosqas.javaweb.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="Customer")
public class Customer {
    @Id
    // makes it auto increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;
}