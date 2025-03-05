package services;

import models.Users;

public class UserSession {
    private static UserSession instance; // Instance unique de la session
    private Users currentUser; // Utilisateur actuellement connecté

    // Constructeur privé pour empêcher l'instanciation directe
    private UserSession() {}

    // Méthode pour obtenir l'instance unique de la session
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Getter et Setter pour l'utilisateur actuel
    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users user) {
        this.currentUser = user;
    }

    // Méthode pour déconnecter l'utilisateur
    public void logout() {
        this.currentUser = null;
    }
}