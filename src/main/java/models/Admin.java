package models;
import java.util.Date;

public class Admin extends User {
    private String position;

    public Admin(int id, String firstname, String lastname, String email, String password, Date joinDate, String avatar, String position) {
        super(id, firstname, lastname, email, password, joinDate, avatar);
        this.position = position;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "position='" + position + '\'' +
                '}';
    }
}