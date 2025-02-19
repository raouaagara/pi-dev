package tn.esprit.Entity;

public class Client extends User {
    private boolean isTobMember;


    public Client(int id, String firstname, String lastname, String email, String password, String avatar, boolean isTobMember) {
        super(id, firstname, lastname, email, password, null, avatar, "CLIENT");
        this.isTobMember = isTobMember;
    }

    public boolean isTobMember() {
        return isTobMember;
    }

    public void setTobMember(boolean isTobMember) {
        this.isTobMember = isTobMember;
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + getId() + ", firstname=" + getFirstname() + ", lastname=" + getLastname() +
                ", email=" + getEmail() + ", isTobMember=" + isTobMember + "}";
    }
}
