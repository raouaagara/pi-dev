package models;

public class Category {
    private int id;
    private String name;
    private String icon;

    // 🔹 Constructeurs
    public Category(int id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public Category(int id, String name) { // Constructeur sans icône
        this.id = id;
        this.name = name;
        this.icon = "";
    }

    public Category(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    // 🔹 Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    // 🔹 Setter pour ID (nécessaire si on récupère l'ID auto-incrémenté)
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name; // Affichage correct dans la ComboBox
    }
}
