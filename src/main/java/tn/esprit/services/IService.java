package tn.esprit.services;

import tn.esprit.entities.Complaint;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {

    void add(T t) throws SQLException;

    void update(T t) throws  SQLException;

    void delete(T t) throws  SQLException;

    List<T> displayList() throws  SQLException;

    T getById(int id) throws SQLException;
    //List<T> search(String keyword) throws SQLException;

}
