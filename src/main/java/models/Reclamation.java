package models;

public class Reclamation {
    private int id;
    private String title;
    private String description;
    private Event event;
    private User user;

    public Reclamation(String title, String description, Event event, User user) {
        this.title = title;
        this.description = description;
        this.event = event;
        this.user = user;
    }
    public Reclamation(int id, String title, String description, Event event, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.event = event;
        this.user = user;
    }

    public Reclamation() {

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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", event=" + event +
                ", user=" + user +
                '}';
    }
}
