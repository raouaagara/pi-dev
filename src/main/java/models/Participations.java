package models;

import java.sql.Timestamp;

public class Participations {
    private int id;
    private int userId;
    private int eventId;
    private Users userName;
    private Events eventName;
    private Timestamp registrationDate;

    public Participations() {
    }

    public Participations(int id, int userId, int eventId, Timestamp registrationDate) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.registrationDate = registrationDate;
    }

    public Participations(int id, Users userName, Events eventName, Timestamp registrationDate) {
        this.id = id;
        this.userName = userName;
        this.eventName = eventName;
        this.registrationDate = registrationDate;

        if (userName != null) {
            this.userId = userName.getId();
        }
        if (eventName != null) {
            this.eventId = eventName.getId();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public Users getUserName() {
        return userName;
    }

    public void setUserName(Users userName) {
        this.userName = userName;
        if (userName != null) {
            this.userId = userName.getId();
        }
    }

    public Events getEventName() {
        return eventName;
    }

    public void setEventName(Events eventName) {
        this.eventName = eventName;
        if (eventName != null) {
            this.eventId = eventName.getId();
        }
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "Participations{" +
                "id=" + id +
                ", userId=" + userId +
                ", eventId=" + eventId +
                ", userName=" + (userName != null ? userName.getUsername() : "null") +
                ", eventName=" + (eventName != null ? eventName.getTitle() : "null") +
                ", registrationDate=" + registrationDate +
                '}';
    }
}