package models;

import java.time.LocalDate;

public class Equipement {
    private int id;
    private String name;
    private String description;
    private int categoryId;
    private float price;
    private String image;
    private boolean availability;
    private LocalDate dateAdded;
    private int partnerId;
    private String categoryName;

    public Equipement(int id, String name, String description, int categoryId, float price, String image, boolean availability, LocalDate dateAdded, int partnerId, String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.image = image;
        this.availability = availability;
        this.dateAdded = dateAdded;
        this.partnerId = partnerId;
        this.categoryName = categoryName;
    }

    // Setters and Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getCategoryId() { return categoryId; }
    public float getPrice() { return price; }
    public String getImage() { return image; }
    public boolean getAvailability() { return availability; }
    public LocalDate getDateAdded() { return dateAdded; }
    public int getPartnerId() { return partnerId; }
    public String getCategoryName() { return categoryName; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public void setPrice(float price) { this.price = price; }
    public void setImage(String image) { this.image = image; }
    public void setAvailability(boolean availability) { this.availability = availability; }
    public void setDateAdded(LocalDate dateAdded) { this.dateAdded = dateAdded; }
    public void setPartnerId(int partnerId) { this.partnerId = partnerId; }
}
