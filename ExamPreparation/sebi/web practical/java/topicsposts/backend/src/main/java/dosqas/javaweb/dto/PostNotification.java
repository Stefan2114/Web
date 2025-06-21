package dosqas.javaweb.dto;

import java.time.LocalDateTime;

public class PostNotification {
    private Integer id;
    private String user;
    private String topicName;
    private String text;
    private LocalDateTime date;

    public PostNotification() {}

    public PostNotification(Integer id, String user, String topicName, String summary, LocalDateTime date) {
        this.id = id;
        this.user = user;
        this.topicName = topicName;
        this.text = summary;
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

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
} 