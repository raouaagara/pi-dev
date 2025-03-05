package services;

import models.Events;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicesEvents implements IServices<Events>{
    Connection cnx;
    public ServicesEvents(){
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Events events) throws SQLException {
        String sql = "INSERT INTO events(title, description, location, startDate, endDate, image) " +
                "VALUES(?, ?, ?, ?, ?, ?)";

        PreparedStatement ste = cnx.prepareStatement(sql);

        ste.setString(1, events.getTitle());
        ste.setString(2, events.getDescription());
        ste.setString(3, events.getLocation());
        ste.setDate(4, new java.sql.Date(events.getStartDate().getTime()));
        ste.setDate(5, new java.sql.Date(events.getEndDate().getTime()));
        ste.setString(6, events.getImage());

        ste.executeUpdate();

        System.out.println("Événement ajouté avec succès !");
    }

    @Override
    public void supprimer(String title ) throws SQLException {
        String sql = "DELETE FROM events WHERE title = ?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setString(1, title);
        st.executeUpdate();
        System.out.println("Événement supprimé avec succès !");
    }

    @Override
    public void modifier(int id, String newTitle, String newDescription, String newLocation,
                         String newStartDate, String newEndDate, String newImage) throws SQLException {
        String sql = "UPDATE events SET title = ?, description = ?, location = ?, startDate = ?, endDate = ?, image = ? WHERE id = ?";

        PreparedStatement st = cnx.prepareStatement(sql);
        st.setString(1, newTitle);
        st.setString(2, newDescription);
        st.setString(3, newLocation);
        st.setString(4, newStartDate);
        st.setString(5, newEndDate);
        st.setString(6, newImage);
        st.setInt(7, id);

        int rowsUpdated = st.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Événement modifié avec succès !");
        } else {
            System.out.println("Aucun événement trouvé avec cet ID.");
        }
    }

    @Override
    public List<Events> recuperer() throws SQLException {
        String sql = "SELECT * FROM events";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);

        List<Events> evenements = new ArrayList<>();

        while (rs.next()) {
            Events ev = new Events();
            ev.setId(rs.getInt("id"));
            ev.setTitle(rs.getString("title"));
            ev.setDescription(rs.getString("description"));
            ev.setLocation(rs.getString("location"));
            ev.setStartDate(rs.getDate("startDate"));
            ev.setEndDate(rs.getDate("endDate"));
            ev.setImage(rs.getString("image"));
            evenements.add(ev);
        }
        return evenements;
    }

    public List<Events> trierParDate() throws SQLException {
        String sql = "SELECT * FROM events ORDER BY startDate ASC";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);

        List<Events> evenements = new ArrayList<>();

        while (rs.next()) {
            Events ev = new Events();
            ev.setId(rs.getInt("id"));
            ev.setTitle(rs.getString("title"));
            ev.setDescription(rs.getString("description"));
            ev.setLocation(rs.getString("location"));
            ev.setStartDate(rs.getDate("startDate"));
            ev.setEndDate(rs.getDate("endDate"));
            ev.setImage(rs.getString("image"));
            evenements.add(ev);
        }
        return evenements;
    }

    public List<Events> rechercherParNom(String nom) throws SQLException {
        String sql = "SELECT * FROM events WHERE title LIKE ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, "%" + nom + "%");
        ResultSet rs = ste.executeQuery();

        List<Events> evenements = new ArrayList<>();

        while (rs.next()) {
            Events ev = new Events();
            ev.setId(rs.getInt("id"));
            ev.setTitle(rs.getString("title"));
            ev.setDescription(rs.getString("description"));
            ev.setLocation(rs.getString("location"));
            ev.setStartDate(rs.getDate("startDate"));
            ev.setEndDate(rs.getDate("endDate"));
            ev.setImage(rs.getString("image"));
            evenements.add(ev);
        }
        return evenements;
    }
}
