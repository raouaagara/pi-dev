package tests;

import models.Event;
import models.Reclamation;
import models.User;
import services.ServiceReclamation;
import tools.MyDataBase;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MyDataBase md = MyDataBase.getInstance();

        try {
            ServiceReclamation serviceReclamation = new ServiceReclamation();

            // Ajouter une réclamation
            Event event = new Event();
            event.setId(1); // Assurez-vous que cet ID existe dans la table Event

            User user = new User();
            user.setId(1); // Assurez-vous que cet ID existe dans la table User

            Reclamation reclamation = new Reclamation(
                    0,
                    "Problème d'organisation",
                    "L'événement a commencé en retard",
                    event,
                    user
            );
            serviceReclamation.ajouter(reclamation);

            // Modifier une réclamation
            // serviceReclamation.modifier(1, "Problème corrigé", "L'événement a été bien organisé finalement.");

            // Récupérer toutes les réclamations
            List<Reclamation> reclamations = serviceReclamation.recupererAll();
            for (Reclamation r : reclamations) {
                System.out.println(r);
            }

            // Supprimer une réclamation
            serviceReclamation.supprimer(1);

        } catch (SQLException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}
