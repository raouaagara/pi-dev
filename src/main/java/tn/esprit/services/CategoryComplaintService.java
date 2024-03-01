package tn.esprit.services;

import tn.esprit.entities.CategoryComplaint;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryComplaintService implements IService<CategoryComplaint> {

    Connection con;
    Statement stm;

    public CategoryComplaintService() {
        con = MyDatabase.getInstance().getCon();
    }

    @Override
    public void add(CategoryComplaint categoryComplaint) throws SQLException {
        String query = "INSERT INTO `categorycomplaint`(`nameCateg`, `userid`) VALUES (?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, categoryComplaint.getNameCateg());
        ps.setInt(2, categoryComplaint.getUserid());
        ps.executeUpdate();
        System.out.println("Category Complaint added!");
    }

    @Override
    public void update(CategoryComplaint categoryComplaint) throws SQLException {
        String query = "UPDATE `categorycomplaint` SET `nameCateg`=? WHERE `idCateg`=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, categoryComplaint.getNameCateg());
        ps.setInt(2, categoryComplaint.getIdCateg());
        ps.executeUpdate();
        System.out.println("Category Complaint updated!");
    }

    @Override
    public List<CategoryComplaint> displayList() throws SQLException {
        String query = "SELECT * FROM `categorycomplaint`";
        stm = con.createStatement();
        ResultSet res = stm.executeQuery(query);
        List<CategoryComplaint> categoryComplaints = new ArrayList<>();
        while (res.next()) {
            CategoryComplaint cc = new CategoryComplaint(
                    res.getInt("idCateg"),
                    res.getString("nameCateg"),
                    res.getInt("userid")
            );
            categoryComplaints.add(cc);
        }
        return categoryComplaints;
    }
    @Override
    public void delete(CategoryComplaint categoryComplaint) throws SQLException {
        String query = "DELETE FROM `categorycomplaint` WHERE `idCateg`=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, categoryComplaint.getIdCateg());
        ps.executeUpdate();
        System.out.println("Category Complaint deleted!");
    }


    @Override
    public CategoryComplaint getById(int id) throws SQLException {
        String query = "SELECT * FROM `categorycomplaint` WHERE `idCateg`=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet res = ps.executeQuery();
        if (res.next()) {
            return new CategoryComplaint(
                    res.getInt("idCateg"),
                    res.getString("nameCateg"),
                    res.getInt("userid")
            );
        }
        return null; // Return null if no category complaint with the given ID is found
    }
    /*@Override
    public List<CategoryComplaint> search(String keyword) throws SQLException {
        String query = "SELECT * FROM `categorycomplaint` WHERE `nameCateg` LIKE ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, "%" + keyword + "%");
        ResultSet res = ps.executeQuery();

        List<CategoryComplaint> searchResults = new ArrayList<>();
        while (res.next()) {
            CategoryComplaint cc = new CategoryComplaint(
                    res.getInt("idCateg"),
                    res.getString("nameCateg"),
                    res.getInt("userid")
            );
            searchResults.add(cc);
        }
        return searchResults;
    }*/

}
