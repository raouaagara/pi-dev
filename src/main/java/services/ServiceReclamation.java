package services;

import com.mysql.cj.xdevapi.JsonParser;
import models.Reclamation;
import models.Event;
import models.Status;
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

    public void modifierEtat(Reclamation reclamation, String answer) throws Exception {
        String sql ="UPDATE reclamation set status='RESOLU',answer=? where id=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, answer);
        reclamation.setAnswer(answer);
        ste.setLong(2, reclamation.getId());
        ste.executeUpdate();
        String notificationSql = "INSERT INTO notification (title, type, onClicked, user_id) VALUES (?, ?, ?, ?)";
        PreparedStatement notifSte = cnx.prepareStatement(notificationSql);
        notifSte.setString(1, "Votre réclamation sous le nom "+reclamation.getTitle()+"  a été résolue.");
        notifSte.setString(2, "Réclamation");
        notifSte.setBoolean(3, false);
        notifSte.setInt(4, reclamation.getUser().getId());
        notifSte.executeUpdate();
        SMSService smsService = new SMSService();
        String toEmail=reclamation.getUser().getEmail();
        String subject="ECOSPORT - Reclamation Resolu";
        String content="<p>Nous avons le plaisir de vous informer que votre réclamation sous le nom " + reclamation.getTitle() + " et la réponse est:\n"+answer+"</p>";
        BrevoEmailService.sendEmail(toEmail,subject,content);
        smsService.sendSMS("+21690102922",answer);
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
        String sql = "SELECT r.id, r.title, r.description, r.status, r.answer, e.id as event_id, e.title as event_title, u.id as user_id " +
                "FROM reclamation r " +
                "LEFT JOIN event e ON r.event_id = e.id " +
                "LEFT JOIN user u ON r.user_id = u.id";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);

        List<Reclamation> reclamations = new ArrayList<>();
        while (rs.next()) {
            // Retrieving the status from the result set
            Status status = Status.valueOf(rs.getString("status"));

            Event event = new Event();
            event.setId(rs.getInt("event_id"));
            event.setTitle(rs.getString("event_title")); // Set the event title

            User user = new User();
            user.setId(rs.getInt("user_id"));

            // Retrieving the answer from the result set
            String answer = rs.getString("answer");

            Reclamation reclamation = new Reclamation(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    status,  // Set the status here
                    answer,  // Set the answer here
                    event,
                    user
            );

            reclamations.add(reclamation);
        }
        return reclamations;
    }



    public Reclamation recupererParId(int id) throws SQLException {
        String sql = "SELECT id, title, description, status, answer, event_id, user_id FROM reclamation WHERE id = ?";

        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id);
        ResultSet rs = ste.executeQuery();

        if (rs.next()) {
            // Retrieving the status from the result set
            Status status = Status.valueOf(rs.getString("status"));

            // Retrieving the answer from the result set
            String answer = rs.getString("answer");

            Reclamation reclamation = new Reclamation();
            reclamation.setId(rs.getInt("id"));
            reclamation.setTitle(rs.getString("title"));
            reclamation.setDescription(rs.getString("description"));
            reclamation.setStatus(status);  // Set the status here
            reclamation.setAnswer(answer);  // Set the answer here

            Event event = new Event();
            event.setId(rs.getInt("event_id"));

            User user = new User();
            user.setId(rs.getInt("user_id"));

            reclamation.setEvent(event);
            reclamation.setUser(user);

            return reclamation;
        } else {
            return null; // Return null if no reclamation is found with the given ID
        }
    }




    public void mettreAJourStatut(Reclamation reclamation) throws Exception {
        String sql = "UPDATE reclamation SET status = 'EN_COURS' WHERE id = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, reclamation.getId());
        int rowsUpdated = ste.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Reclamation mettre ajour de statut");
            String notificationSql = "INSERT INTO notification (title, type, onClicked, user_id) VALUES (?, ?, ?, ?)";
            PreparedStatement notifSte = cnx.prepareStatement(notificationSql);
            notifSte.setString(1, "Votre réclamation sous le nom "+reclamation.getTitle()+" est en cours de traitement.");
            notifSte.setString(2, "Réclamation");
            notifSte.setBoolean(3, false);
            notifSte.setInt(4, reclamation.getUser().getId());
            notifSte.executeUpdate();
            String toEmail=reclamation.getUser().getEmail();
            String subject="ECOSPORT - Reclamation est en cours de traitement";
            String content="<p>Nous avons le plaisir de vous informer que votre réclamation sous le nom " + reclamation.getTitle() + " est en cours de traitement.</p>";
            BrevoEmailService.sendEmail(toEmail,subject,content);
        }
        else {
            System.out.println("Reclamation introuvable !");

        }

    }
}
