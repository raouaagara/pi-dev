package services;

import models.Users;
import tools.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServicesUsers {
    private Connection cnx;

    public ServicesUsers() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    public Users authenticate(String email, String password) {
        String sql = "SELECT * FROM Users WHERE email = ? AND password = ?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setString(1, email);
            ste.setString(2, password);

            ResultSet rs = ste.executeQuery();
            if (rs.next()) {
                Users user = new Users();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}