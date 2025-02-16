package tools;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    private static MyDataBase instance;
    private Connection connection;
    private final String URL = "jdbc:mysql://localhost:3306/ecosport";
    private final String USER = "root";  // Mets ton utilisateur MySQL
    private final String PASSWORD = "";  // Mets ton mot de passe MySQL

    private MyDataBase() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion réussie !");
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
        }
    }

    public static MyDataBase getInstance() {
        if (instance == null) {
            instance = new MyDataBase();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
