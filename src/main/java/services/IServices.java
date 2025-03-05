package services;

import models.Events;

import java.sql.SQLException;
import java.util.List;

public interface IServices<T> {
    void ajouter(T t) throws SQLException;
    void supprimer(String title) throws SQLException;
    void modifier(int id, String Title, String Description, String Location,
                  String StartDate, String EndDate, String Image) throws SQLException;
    List<T> recuperer() throws SQLException;
    List<Events> trierParDate() throws SQLException;
    List<Events> rechercherParNom(String nom) throws SQLException;
}
