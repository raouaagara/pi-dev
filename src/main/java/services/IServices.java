package services;

import models.Reclamation;

import java.sql.SQLException;
import java.util.List;

public interface IServices <T>{

    void ajouter(T t) throws SQLException;
    void supprimer(int id) throws SQLException;
    void modifier(T t) throws SQLException;
    List<T> recupererAll() throws SQLException;
    T recupererParId(int id) throws SQLException;
    }
