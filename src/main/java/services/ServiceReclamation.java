package services;

import com.mysql.cj.xdevapi.JsonParser;
import models.Reclamation;
import models.Event;
import models.User;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReclamation implements IServices<Reclamation> {
    private Connection cnx;

    public ServiceReclamation() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    public void modifierEtat(Reclamation reclamation, String etat, String answer) throws SQLException {
        String sql ="UPDATE reclamation set status=?,answer=? where id=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, etat);
        ste.setString(2, answer);

        ste.setLong(3, reclamation.getId());
        ste.executeUpdate();
        System.out.println("Reclamation modified");

    }


    public void ajouter(Reclamation reclamation) throws SQLException {
        String sql = "INSERT INTO reclamation (title, description, event_id, user_id) VALUES (?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, reclamation.getTitle());
        ste.setString(2, reclamation.getDescription());
        ste.setInt(3, reclamation.getEvent().getId());
        ste.setInt(4, reclamation.getUser().getId());
        ste.executeUpdate();
        System.out.println("Reclamation ajoutée avec succès !");
    }

    public void modifier(Reclamation reclamation) throws SQLException {
        // Fetch the reclamation from the database by its ID
        Reclamation existingReclamation = recupererParId(reclamation.getId());

        if (existingReclamation != null) {
            // Update the attributes of the existing reclamation
            String sql = "UPDATE reclamation SET title = ?, description = ?, event_id = ?, user_id = ? WHERE id = ?";
            PreparedStatement ste = cnx.prepareStatement(sql);
            ste.setString(1, reclamation.getTitle());
            ste.setString(2, reclamation.getDescription());
            ste.setInt(3, reclamation.getEvent().getId()); // Ensure the Event ID is updated
            ste.setInt(4, reclamation.getUser().getId());  // Ensure the User ID is updated
            ste.setInt(5, reclamation.getId());

            int rowsUpdated = ste.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Reclamation modifiée avec succès !");
            } else {
                System.out.println("Aucune réclamation trouvée avec cet ID.");
            }
        } else {
            System.out.println("Réclamation introuvable !");
        }
    }


    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM reclamation WHERE id = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id);
        int rowsDeleted = ste.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Reclamation supprimée avec succès !");
        } else {
            System.out.println("Aucune réclamation trouvée avec cet ID.");
        }
    }

    public List<Reclamation> recupererAll() throws SQLException {
        String sql = "SELECT r.id, r.title, r.description, e.id as event_id, e.title as event_title, u.id as user_id FROM reclamation r " +
                "LEFT JOIN event e ON r.event_id = e.id " +
                "LEFT JOIN user u ON r.user_id = u.id";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);

        List<Reclamation> reclamations = new ArrayList<>();
        while (rs.next()) {
            Event event = new Event();
            event.setId(rs.getInt("event_id"));
            event.setTitle(rs.getString("event_title")); // Set the event title

            User user = new User();
            user.setId(rs.getInt("user_id"));

            Reclamation reclamation = new Reclamation(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    event,
                    user
            );

            reclamations.add(reclamation);
        }
        return reclamations;
    }

    public Reclamation recupererParId(int id) throws SQLException {
        String sql = "SELECT id, title, description, event_id, user_id FROM reclamation WHERE id = ?";

        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id);
        ResultSet rs = ste.executeQuery();

        if (rs.next()) {
            // Create a basic Reclamation object with only IDs for Event and User
            Reclamation reclamation = new Reclamation();
            reclamation.setId(rs.getInt("id"));
            reclamation.setTitle(rs.getString("title"));
            reclamation.setDescription(rs.getString("description"));

            // Create minimal Event and User objects with only IDs
            Event event = new Event();
            event.setId(rs.getInt("event_id"));

            User user = new User();
            user.setId(rs.getInt("user_id"));

            // Set the Event and User objects in the Reclamation
            reclamation.setEvent(event);
            reclamation.setUser(user);

            return reclamation;
        } else {
            return null; // Return null if no reclamation is found with the given ID
        }
    }


}
