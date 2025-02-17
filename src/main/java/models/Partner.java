package models;
import java.util.Date;

public class Partner extends User {
    private String organizationName;

    public Partner(int id, String firstname, String lastname, String email, String password, Date joinDate, String avatar, String organizationName) {
        super(id, firstname, lastname, email, password, joinDate, avatar);
        this.organizationName = organizationName;
    }

    public Partner() {
        super();
    }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    @Override
    public String toString() {
        return super.toString() + ", Partner{organizationName='" + organizationName + "'}";
    }
}