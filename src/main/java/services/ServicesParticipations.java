package services;

import models.Events;
import models.Participations;
import models.Users;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicesParticipations {
    private Connection cnx;

    public ServicesParticipations() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    public void eventParticipation(int userId, int eventId) throws SQLException {
        String sql = "INSERT INTO Participations (userId, eventId, registrationDate) VALUES (?, ?, ?)";

        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, userId);
            ste.setInt(2, eventId);
            ste.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

            int rowsInserted = ste.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("L'utilisateur a été inscrit à l'événement avec succès !");
            } else {
                System.out.println("Erreur lors de l'inscription à l'événement.");
            }
        }
    }

    public List<Participations> recupererParticipations() throws SQLException {
        String sql = "SELECT Participations.id, Users.username, Events.title, Participations.registrationDate " +
                "FROM Participations " +
                "JOIN Users ON Participations.userId = Users.id " +
                "JOIN Events ON Participations.eventId = Events.id";

        List<Participations> participations = new ArrayList<>();

        try (Statement ste = cnx.createStatement();
             ResultSet rs = ste.executeQuery(sql)) {

            while (rs.next()) {
                Participations p = new Participations();
                p.setId(rs.getInt("id"));

                Users user = new Users(1, "tchoffo Joan", "john@example.com", "password", "2025-03-04");
                user.setUsername(rs.getString("username"));
                p.setUserName(user);

                Events event = new Events();
                event.setTitle(rs.getString("title"));
                p.setEventName(event);

                p.setRegistrationDate(rs.getTimestamp("registrationDate"));

                participations.add(p);
            }
        }
        return participations;
    }

    public boolean eventExists(int eventId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM events WHERE id = ?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, eventId);
            ResultSet rs = ste.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}