package models;

public class Reclamation {
    private int id;
    private String title;
    private String description;
    private Status status = Status.EN_ATTENTE;
    private String answer;
    private Event event;
    private User user;


    public Reclamation(int id, String title, String description, Status status, String answer, Event event, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.answer = answer;
        this.event = event;
        this.user = user;
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", answer='" + answer + '\'' +
                ", event=" + event +
                ", user=" + user +
                '}';
    }
}
