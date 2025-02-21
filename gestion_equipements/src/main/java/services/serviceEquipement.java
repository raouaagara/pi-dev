package services;
import models.Equipement;
import tools.MyDataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Category;

public class serviceEquipement {
    private final Connection con;

    public serviceEquipement() {
        con = MyDataBase.getInstance().getConnection();
    }

    // Ajouter un équipement
    public void ajouter(Equipement e) {
        String sql = "INSERT INTO equipement (name, description, categoryId, price, image, availability, dateAdded, partnerId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getName());
            ps.setString(2, e.getDescription());
            ps.setInt(3, e.getCategory().getId()); // Correction ici
            ps.setFloat(4, e.getPrice());
            ps.setString(5, e.getImage());
            ps.setBoolean(6, e.isAvailable()); // Correction ici
            ps.setDate(7, Date.valueOf(e.getDateAdded()));
            ps.setInt(8, e.getPartnerId());

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Équipement ajouté avec succès !");
            } else {
                System.err.println("⚠️ Aucun équipement n'a été ajouté.");
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur SQL lors de l'ajout : " + ex.getMessage());
        }
    }



    // Modifier un équipement
    public void modifier(Equipement e) {
        String sql = "UPDATE equipement SET name=?, description=?, categoryId=?, price=?, image=?, availability=?, dateAdded=?, partnerId=? WHERE id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getName());
            ps.setString(2, e.getDescription());
            ps.setInt(3, e.getCategory().getId()); // Correction ici
            ps.setFloat(4, e.getPrice());
            ps.setString(5, e.getImage());
            ps.setBoolean(6, e.isAvailable());
            ps.setDate(7, Date.valueOf(e.getDateAdded()));
            ps.setInt(8, e.getPartnerId());
            ps.setInt(9, e.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Équipement mis à jour avec succès !");
            } else {
                System.err.println("⚠️ Mise à jour échouée.");
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur SQL lors de la mise à jour : " + ex.getMessage());
        }
    }

    // Supprimer un équipement
    public void supprimer(int id) {
        String sql = "DELETE FROM equipement WHERE id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Équipement supprimé avec succès !");
            } else {
                System.err.println("⚠️ Suppression échouée.");
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur SQL lors de la suppression : " + ex.getMessage());
        }
    }

    // Récupérer un équipement par ID
    public Equipement getById(int id) {
        String sql = "SELECT e.*, c.name AS categoryName FROM equipement e JOIN category c ON e.categoryId = c.id WHERE e.id=?";
        Equipement equipement = null;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Création de l'objet Category
                Category category = new Category(rs.getInt("categoryId"), rs.getString("categoryName"));

                // Création de l'objet Equipement avec la catégorie
                equipement = new Equipement(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        category, // Correction ici : on passe un objet Category au lieu de categoryId seul
                        rs.getFloat("price"),
                        rs.getString("image"),
                        rs.getBoolean("availability"),
                        rs.getDate("dateAdded").toLocalDate(),
                        rs.getInt("partnerId")
                );
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur SQL lors de la récupération : " + ex.getMessage());
        }

        return equipement;
    }

    // Récupérer tous les équipements
    public List<Equipement> getAll() {
        List<Equipement> equipements = new ArrayList<>();
        String sql = "SELECT e.id, e.name, e.description, e.categoryId, e.price, e.image, e.availability, e.dateAdded, e.partnerId, c.name AS categoryName " +
                "FROM equipement e " +
                "JOIN category c ON e.categoryId = c.id";

        try (PreparedStatement statement = con.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                // ✅ Créer un objet Category avec les données récupérées
                Category category = new Category(rs.getInt("categoryId"), rs.getString("categoryName"));

                // ✅ Créer un objet Equipement avec l'objet Category
                Equipement equipement = new Equipement(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        category, // Ici on passe un objet Category, pas un ID et un nom séparés
                        rs.getFloat("price"),
                        rs.getString("image"),
                        rs.getBoolean("availability"),
                        rs.getDate("dateAdded").toLocalDate(),
                        rs.getInt("partnerId")
                );

                equipements.add(equipement);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL lors de la récupération des équipements : " + e.getMessage());
        }

        return equipements;
    }

    public List<Equipement> getEquipementsSortedByPriceDesc() {
        List<Equipement> equipements = new ArrayList<>();
        String sql = "SELECT e.id, e.name, e.description, e.categoryId, e.price, e.image, e.availability, e.dateAdded, e.partnerId, c.name AS categoryName " +
                "FROM equipement e " +
                "JOIN category c ON e.categoryId = c.id " +
                "ORDER BY e.price DESC"; // Trie par prix décroissant

        try (PreparedStatement statement = con.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                // ✅ Créer un objet Category à partir des données de la base de données
                Category category = new Category(rs.getInt("categoryId"), rs.getString("categoryName"));

                // ✅ Utiliser l'objet Category pour instancier Equipement
                Equipement equipement = new Equipement(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        category, // ✅ On passe un objet Category, pas un int !
                        rs.getFloat("price"),
                        rs.getString("image"),
                        rs.getBoolean("availability"),
                        rs.getDate("dateAdded").toLocalDate(),
                        rs.getInt("partnerId")
                );

                equipements.add(equipement);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL lors de la récupération des équipements triés par prix : " + e.getMessage());
        }

        return equipements;
    }



}
