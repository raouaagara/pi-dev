package models;
import java.util.Date;

public class Article {
    private int id;
    private String title;
    private String description;
    private int authorId;
    private Date publishDate;
    private String image;

    public Article(int id, String title, String description, int authorId, Date publishDate, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.authorId = authorId;
        this.publishDate = publishDate;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

@Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", authorId=" + authorId +
                ", publishDate=" + publishDate +
                ", image='" + image + '\'' +
                '}';
    }
}