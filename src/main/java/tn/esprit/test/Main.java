package tn.esprit.test;

import tn.esprit.entities.CategoryComplaint;
import tn.esprit.entities.Complaint;
import tn.esprit.services.CategoryComplaintService;
import tn.esprit.services.ComplaintService;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize services
        ComplaintService complaintService = new ComplaintService();
        CategoryComplaintService categoryComplaintService = new CategoryComplaintService();

        // Add category complaint
        CategoryComplaint cc1 = new CategoryComplaint(1, "Category 1", 101);
        try {
            categoryComplaintService.add(cc1);
            System.out.println("Category complaint added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding category complaint: " + e.getMessage());
        }

        // Display list of category complaints
        try {
            System.out.println("List of category complaints:");
            List<CategoryComplaint> categoryComplaints = categoryComplaintService.displayList();
            for (CategoryComplaint categoryComplaint : categoryComplaints) {
                System.out.println(categoryComplaint);
            }
        } catch (SQLException e) {
            System.out.println("Error displaying category complaints: " + e.getMessage());
        }

        // Update a category complaint
        CategoryComplaint ccToUpdate = new CategoryComplaint(1, "Updated Category 1", 101);
        try {
            categoryComplaintService.update(ccToUpdate);
            System.out.println("Category complaint updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating category complaint: " + e.getMessage());
        }

        // Delete a category complaint
        try {
            categoryComplaintService.delete(cc1);
            System.out.println("Category complaint deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting category complaint: " + e.getMessage());
        }

        // User input for complaint management (as in the original code)
        Scanner scanner = new Scanner(System.in);

        // Handling of complaints
        // ...

        scanner.close();
    }
}






