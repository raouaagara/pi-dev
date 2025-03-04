package models;
import java.util.Date;

public class Equipement {
    private int id;
    private String name;
    private String description;
    private String category;
    private float price;
    private String image;
    private boolean availability;
    private Date dateAdded;
    private int partnerId;

    public Equipement(int id, String name, String description, String category, float price, String image, boolean availability, Date dateAdded, int partnerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.image = image;
        this.availability = availability;
        this.dateAdded = dateAdded;
        this.partnerId = partnerId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public float getPrice() { return price; }
    public String getImage() { return image; }
    public boolean isAvailability() { return availability; }
    public Date getDateAdded() { return dateAdded; }
    public int getPartnerId() { return partnerId; }

    @Override
    public String toString() {
        return "Equipement{id=" + id + ", name='" + name + "', price=" + price + "}";
    }
}