package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import models.Events;
import models.Users;
import services.ServicesEvents;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class frontListEventController {
    @FXML
    private GridPane eventsContainer;

    @FXML
    private TextField searchField;

    private ServicesEvents servicesEvents;
    private Users currentUser;

    public frontListEventController() {
        servicesEvents = new ServicesEvents();
    }

    public void setCurrentUser(Users user) {
        this.currentUser = user;
    }

    @FXML
    public void initialize() {
        loadEvents();
    }

    private void loadEvents() {
        try {
            List<Events> events = servicesEvents.recuperer();
            int column = 0;
            int row = 0;
            for (Events event : events) {
                eventsContainer.add(createEventCard(event), column, row);
                column++;
                if (column == 3) {
                    column = 0;
                    row++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox createEventCard(Events event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/eventCard.fxml"));
            VBox eventCard = loader.load();

            EventCardController controller = loader.getController();
            controller.setEventData(event, currentUser);

            return eventCard;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText();
        try {
            List<Events> events = servicesEvents.rechercherParNom(searchText);
            eventsContainer.getChildren().clear();
            int column = 0;
            int row = 0;
            for (Events event : events) {
                eventsContainer.add(createEventCard(event), column, row);
                column++;
                if (column == 3) {
                    column = 0;
                    row++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSortByDate() {
        try {
            List<Events> events = servicesEvents.trierParDate();
            eventsContainer.getChildren().clear();
            int column = 0;
            int row = 0;
            for (Events event : events) {
                eventsContainer.add(createEventCard(event), column, row);
                column++;
                if (column == 3) {
                    column = 0;
                    row++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}