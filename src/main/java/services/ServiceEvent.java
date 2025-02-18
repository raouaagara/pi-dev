package services;

import models.Event;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvent {
    private Connection cnx;

    public ServiceEvent() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    public List<Event> fetchAllEvents() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM Event"; // Adjust the table name if necessary

        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Event event = new Event();
                event.setId(rs.getInt("id"));
                event.setTitle(rs.getString("title"));
                event.setDescription(rs.getString("description"));
                event.setStartDate(rs.getDate("startDate"));
                event.setEndDate(rs.getDate("endDate"));
                event.setLocation(rs.getString("location"));
                event.setImage(rs.getString("image"));

                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }
}