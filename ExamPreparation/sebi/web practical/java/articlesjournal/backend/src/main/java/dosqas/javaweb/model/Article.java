package dosqas.javaweb.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String user;

    @ManyToOne
    // joins on the id (primary key) column of the journal entity
    // names it journalid
    // if we wanted on another column we d specify the referencedColumnName
    @JoinColumn(name = "journalid", nullable = false)
    private Journal journal;

    @Column(nullable = false)
    private String summary;

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}