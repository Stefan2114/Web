package dosqas.javaweb.dto;

public class AuthoredWorkDTO {

    private int id;
    private String name;
    private String contents;
    private String title;
    private Integer duration;
    private Boolean isMovie = false;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getContents() {
        return contents;
    }
    public void setContents(String contents) {
        this.contents = contents;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getDuration() {
        return duration;
    }
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    public Boolean getIsMovie() {
        return isMovie;
    }
    public void setIsMovie(Boolean isMovie) {
        this.isMovie = isMovie;
    }
}

