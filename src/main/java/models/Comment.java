package models;
import java.util.Date;

public class Comment {
    private int id;
    private String content;
    private int userId;
    private int articleId;
    private Date publishDate;

    public Comment(int id, String content, int userId, int articleId, Date publishDate) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.articleId = articleId;
        this.publishDate = publishDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", userId=" + userId +
                ", articleId=" + articleId +
                ", publishDate=" + publishDate +
                '}';
    }
}