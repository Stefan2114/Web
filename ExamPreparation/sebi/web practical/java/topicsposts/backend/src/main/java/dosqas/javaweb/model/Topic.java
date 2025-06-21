package dosqas.javaweb.model;

import jakarta.persistence.*;

@Entity
public class Topic {
    @Id
    // makes it auto increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String topicName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName (String name) {
        this.topicName = name;
    }
}
