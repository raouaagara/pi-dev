package models;
import java.util.Date;
public class Client extends User {
    private boolean isTopMember;

    public Client(int id, String firstname, String lastname, String email, String password, Date joinDate, String avatar, boolean isTopMember) {
        super(id, firstname, lastname, email, password, joinDate, avatar);
        this.isTopMember = isTopMember;
    }

    public boolean isTopMember() { return isTopMember; }
    public void setTopMember(boolean topMember) { isTopMember = topMember; }

    @Override
    public String toString() {
        return super.toString() + ", Client{isTopMember=" + isTopMember + "}";
    }
}