package models;

public enum Status {
    EN_ATTENTE("En Attente"),
    EN_COURS("En Cours"),
    RESOLU("Résolu");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}