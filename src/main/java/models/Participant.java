package models;
import java.util.Date;

public class Participant {
    private int participantId;
    private int eventId;
    private Date registrationDate;

    public Participant(int participantId, int eventId, Date registrationDate) {
        this.participantId = participantId;
        this.eventId = eventId;
        this.registrationDate = registrationDate;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "participantId=" + participantId +
                ", eventId=" + eventId +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
