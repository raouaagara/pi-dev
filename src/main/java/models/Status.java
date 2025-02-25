package models;

public enum Status {
    PENDING, IN_PROGRESS, RESOLVED, REJECTED;

    // Méthode pour afficher les statuts en français
    public String getFrenchLabel() {
        return switch (this) {
            case PENDING -> "En attente";
            case IN_PROGRESS -> "En cours";
            case RESOLVED -> "Résolue";
            case REJECTED -> "Rejetée";
        };
    }
}
