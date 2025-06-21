package dosqas.javaweb.dto;

import java.time.LocalDateTime;

// DTO used to send notifications to observers, contains the info needed to
// display the notification. useful as we don't need it as an object in our
// database, just to send it over
public class ArticleNotification {
    private Integer id;
    private String user;
    private String journalName;
    private String summary;
    private LocalDateTime date;

    public ArticleNotification() {}

    public ArticleNotification(Integer id, String user, String journalName, String summary, LocalDateTime date) {
        this.id = id;
        this.user = user;
        this.journalName = journalName;
        this.summary = summary;
        this.date = date;
    }

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

    public String getJournalName() {
        return journalName;
    }

    public void setJournalName(String journalName) {
        this.journalName = journalName;
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