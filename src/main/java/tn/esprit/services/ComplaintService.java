package tn.esprit.services;
import java.io.IOException;
import java.sql.ResultSet;
import tn.esprit.entities.Complaint;
import tn.esprit.utils.MyDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


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

        if (complaint.getDescription().length() > 2000) {
            throw new IllegalArgumentException("Description cannot exceed 2000 characters");
        }

        // Add the complaint to the database
        Connection con = MyDatabase.getInstance().getCon(); // Access con from the instance
        String query = "INSERT INTO `complaint`(`title`, `description`, `category`, `location`, `status`, `user`,`imagePath`) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, complaint.getTitle());
        ps.setString(2, complaint.getDescription());
        ps.setString(3, complaint.getCategory());
        ps.setString(4, complaint.getLocation());
        ps.setString(5, complaint.getStatus());
        ps.setString(6, complaint.getUser());
        ps.setString(7, complaint.getImagePath());
        ps.executeUpdate();
        System.out.println("Complaint added!");
    }


    @Override
    public void update(Complaint complaint) throws SQLException {
        String query = "UPDATE `complaint` SET `title`=?, `description`=?, `category`=?, `location`=?, `status`=?, `datePosted`=?, `user`=?,`imagePath`=? WHERE `complaintId`=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, complaint.getTitle());
        ps.setString(2, complaint.getDescription());
        ps.setString(3, complaint.getCategory());
        ps.setString(4, complaint.getLocation());
        ps.setString(5, complaint.getStatus());
        ps.setDate(6, new java.sql.Date(complaint.getDatePosted().getTime()));
        ps.setString(7, complaint.getUser());
        ps.setString(8, complaint.getImagePath());
        ps.setInt(9, complaint.getComplaintId());
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
                    res.getDate("datePosted"),
                    res.getString("user"),
                    res.getString("imagePath")
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
                    res.getDate("datePosted"),
                    res.getString("user"),
                    res.getString("imagePath")
            );
        }
        return null; // Return null if no complaint with the given ID is found
    }


    public List<Complaint> search(String titleKeyword, String locationKeyword, String statusKeyword) throws SQLException {
        List<Complaint> searchResults = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT * FROM complaint WHERE title LIKE ? AND location LIKE ? AND status LIKE ?";
            statement = con.prepareStatement(query);
            statement.setString(1, "%" + titleKeyword + "%");
            statement.setString(2, "%" + locationKeyword + "%");
            statement.setString(3, "%" + statusKeyword + "%");

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int complaintId = resultSet.getInt("complaint_id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String category = resultSet.getString("category");
                String location = resultSet.getString("location");
                String status = resultSet.getString("status");
                java.util.Date datePosted = resultSet.getDate("date_posted");
                String user = resultSet.getString("user");
                String imagePath = resultSet.getString("imagePath");


                Complaint complaint = new Complaint(complaintId, title, description, category, location, status, datePosted, user, imagePath);
                searchResults.add(complaint);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }

        return searchResults;
    }

    public void sort(List<Complaint> complaints, String sortBy) {
        switch (sortBy.toLowerCase()) {
            case "date posted":
                // Sort by date posted
                Collections.sort(complaints, Comparator.comparing(Complaint::getDatePosted));
                break;
            case "location":
                // Sort by location
                Collections.sort(complaints, Comparator.comparing(Complaint::getLocation));
                break;
            case "status":
                // Sort by status
                Collections.sort(complaints, Comparator.comparing(Complaint::getStatus));
                break;

        }
    }

    public double calculateAverageScore() throws SQLException {
        String query = "SELECT SUM(score) AS totalScore, COUNT(*) AS totalCount FROM complaint";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet res = ps.executeQuery();

        if (res.next()) {
            int totalScore = res.getInt("totalScore");
            int totalCount = res.getInt("totalCount");

            if (totalCount > 0) {
                return (double) totalScore / totalCount;
            } else {
                return 0; // Return 0 if there are no complaints
            }
        }
        return 0;
    }
}

