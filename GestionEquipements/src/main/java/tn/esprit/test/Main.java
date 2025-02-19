package tn.esprit.test;

import tn.esprit.Entity.User;
import tn.esprit.services.UserService;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();

        User user = new User("John", "Doe", "john.doe@example.com", "hashed_password");

        userService.addUser(user);
        System.out.println("Utilisateur ajouté avec succès !");
    }
}
