package services;

import models.Notification;
import models.User;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceNotification {
    private Connection cnx;

    public ServiceNotification() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    public List<Notification> recupererParUtilisateur(User user) throws SQLException {
        String sql = "SELECT * FROM notification WHERE user_id = ? ORDER BY id DESC";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, user.getId());
        ResultSet rs = ste.executeQuery();

        List<Notification> notifications = new ArrayList<>();
        while (rs.next()) {
            Notification notification = new Notification(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("type"),
                    rs.getBoolean("onClicked"),
                    user
            );
            notifications.add(notification);
        }
        return notifications;
    }
}
