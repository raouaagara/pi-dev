package services;

import models.Category;
import tools.MyDataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private Connection connection;

    public CategoryService() {
        connection = MyDataBase.getInstance().getConnection();
    }

    // Ajouter une catégorie et récupérer son ID
    public void addCategory(Category category) {
        String query = "INSERT INTO category (name, icon) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getIcon());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                category.setId(rs.getInt(1));  // Met à jour l'ID auto-généré
            }

            System.out.println("Catégorie ajoutée avec succès ! ID: " + category.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Récupérer toutes les catégories
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM category";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                categories.add(new Category(rs.getInt("id"), rs.getString("name"), rs.getString("icon")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    // Supprimer une catégorie par ID
    public void deleteCategory(int id) {
        String query = "DELETE FROM category WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Catégorie supprimée avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
