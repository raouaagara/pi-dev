package tn.esprit.test;
import tn.esprit.entities.Complaint;
import tn.esprit.services.ComplaintService;
import tn.esprit.utils.MyDatabase;
import java.util.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ComplaintService complaintService = new ComplaintService();
        Complaint c1 = new Complaint(
                1,
                "complaint!!",
                "hello",
                "pollution",
                "esprit",
                "not repaired",
                "User of the complaint"
        );

        try {
            // Add a new complaint
            complaintService.add(c1);
            System.out.println("Complaint added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding complaint: " + e.getMessage());
        }

        try {
            // Display list of complaints after adding
            List<Complaint> complaints = complaintService.displayList();
            System.out.println("List of complaints:");
            for (Complaint complaint : complaints) {
                System.out.println(complaint);
            }
        } catch (SQLException e) {
            System.out.println("Error displaying complaints: " + e.getMessage());
        }


        Scanner scanner = new Scanner(System.in);

        // Update a complaint
        System.out.print("Enter the ID of the complaint to update: ");
        int updateId = scanner.nextInt();
        scanner.nextLine(); // consume newline
        try {
            Complaint complaintToUpdate = complaintService.getById(updateId);
            if (complaintToUpdate != null) {
                System.out.print("Enter the updated title: ");
                String updatedTitle = scanner.nextLine();
                complaintToUpdate.setTitle(updatedTitle);
                complaintService.update(complaintToUpdate);
                System.out.println("Complaint updated successfully!");
            } else {
                System.out.println("Complaint not found with ID: " + updateId);
            }
        } catch (SQLException e) {
            System.out.println("Error updating complaint: " + e.getMessage());
        }

        // Delete a complaint
        System.out.print("Enter the ID of the complaint to delete: ");
        int deleteId = scanner.nextInt();
        scanner.nextLine(); // consume newline
        try {
            Complaint complaintToDelete = complaintService.getById(deleteId);
            if (complaintToDelete != null) {
                complaintService.delete(complaintToDelete);
                System.out.println("Complaint deleted successfully!");
            } else {
                System.out.println("Complaint not found with ID: " + deleteId);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting complaint: " + e.getMessage());
        }

        scanner.close();
    }
}


