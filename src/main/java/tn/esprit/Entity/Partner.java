package tn.esprit.Entity;

public class Partner extends User {
    private String organizationName;


    public Partner(int id, String firstname, String lastname, String email, String password, String avatar, String organizationName) {
        super(id, firstname, lastname, email, password, null, avatar, "PARTNER");
        this.organizationName = organizationName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @Override
    public String toString() {
        return "Partner{" + "id=" + getId() + ", firstname=" + getFirstname() + ", lastname=" + getLastname() +
                ", email=" + getEmail() + ", organizationName=" + organizationName + "}";
    }
}
