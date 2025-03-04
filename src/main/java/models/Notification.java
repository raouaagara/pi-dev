package models;

public class Notification {
    int id;
    String title;
    String type;
    boolean onClicked = false;
    User user = null;

    public Notification(int id, String title, String type, boolean onClicked, User user) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.onClicked = onClicked;
        this.user = user;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOnClicked() {
        return onClicked;
    }

    public void setOnClicked(boolean onClicked) {
        this.onClicked = onClicked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "title='" + title + '\'' +
                ", onClicked=" + onClicked +
                ", user=" + user +
                '}';
    }
}
