package tn.esprit.Entity;

public class Admin extends User {
    private String position;


    public Admin(int id, String firstname, String lastname, String email, String password, String avatar, String position) {
        super(id, firstname, lastname, email, password, null, avatar, "ADMIN");
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Admin{" + "id=" + getId() + ", firstname=" + getFirstname() + ", lastname=" + getLastname() +
                ", email=" + getEmail() + ", position=" + position + "}";
    }
}
