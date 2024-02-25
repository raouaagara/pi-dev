package tn.esprit.services;
import java.sql.ResultSet;
import tn.esprit.entities.Complaint;
import tn.esprit.utils.MyDatabase;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

public class ComplaintService implements IService<Complaint> {

    Connection con;
    Statement stm;

    public ComplaintService() {
        con = MyDatabase.getInstance().getCon();
    }
    @Override
    public void add(Complaint complaint) throws SQLException {
        // Validate input fields
        if (complaint.getTitle() == null || complaint.getTitle().isEmpty() ||
                complaint.getDescription() == null || complaint.getDescription().isEmpty() ||
                complaint.getCategory() == null || complaint.getCategory().isEmpty() ||
                complaint.getLocation() == null || complaint.getLocation().isEmpty() ||
                complaint.getStatus() == null || complaint.getStatus().isEmpty() ||
                complaint.getUser() == null || complaint.getUser().isEmpty()) {
            throw new IllegalArgumentException("Please fill in all fields");
        }

        // Additional validation for description length (maximum length of 200 characters)
        if (complaint.getDescription().length() > 200) {
            throw new IllegalArgumentException("Description cannot exceed 200 characters");
        }

        // Add the complaint to the database
        String query = "INSERT INTO `complaint`(`title`, `description`, `category`, `location`, `status`, `user`) VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, complaint.getTitle());
        ps.setString(2, complaint.getDescription());
        ps.setString(3, complaint.getCategory());
        ps.setString(4, complaint.getLocation());
        ps.setString(5, complaint.getStatus());
        ps.setString(6, complaint.getUser());
        ps.executeUpdate();
        System.out.println("Complaint added!");
    }


    @Override
    public void update(Complaint complaint) throws SQLException {
        String query = "UPDATE `complaint` SET `title`=?, `description`=?, `category`=?, `location`=?, `status`=?, `datePosted`=?, `user`=? WHERE `complaintId`=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, complaint.getTitle());
        ps.setString(2, complaint.getDescription());
        ps.setString(3, complaint.getCategory());
        ps.setString(4, complaint.getLocation());
        ps.setString(5, complaint.getStatus());
        //ps.setDate(6, new java.sql.Date(complaint.getDatePosted().getTime()));
        ps.setString(6, complaint.getUser());
        ps.setInt(7, complaint.getComplaintId());
        ps.executeUpdate();
        System.out.println("Complaint updated!");
    }

    @Override
    public void delete(Complaint complaint) throws SQLException {
        String query = "DELETE FROM `complaint` WHERE `complaintId`=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, complaint.getComplaintId());
        ps.executeUpdate();
        System.out.println("Complaint deleted!");
    }

    @Override
    public List<Complaint> displayList() throws SQLException {
        String query = "SELECT * FROM `complaint`";
        stm = con.createStatement();
        ResultSet res = stm.executeQuery(query);
        List<Complaint> complaints = new ArrayList<>();
        while (res.next()) {
            Complaint c = new Complaint(
                    res.getInt("complaintId"),
                    res.getString("title"),
                    res.getString("description"),
                    res.getString("category"),
                    res.getString("location"),
                    res.getString("status"),
                   // res.getDate("datePosted"),
                    res.getString("user")
            );
            complaints.add(c);
        }
        return complaints;
    }

    @Override
    public Complaint getById(int id) throws SQLException {
        String query = "SELECT * FROM `complaint` WHERE `complaintId`=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet res = ps.executeQuery();
        if (res.next()) {
            return new Complaint(
                    res.getInt("complaintId"),
                    res.getString("title"),
                    res.getString("description"),
                    res.getString("category"),
                    res.getString("location"),
                    res.getString("status"),
                   // res.getDate("datePosted"),
                    res.getString("user")
            );
        }
        return null; // Return null if no complaint with the given ID is found
    }

}
