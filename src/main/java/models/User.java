package models;

import java.util.Date;

public class User {
    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Date joinDate;
    private String avatar;
    public static final int TOPMEMBER = 5;

    public User() {}

    public User(int id, String firstname, String lastname, String email, String password, Date joinDate, String avatar) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.joinDate = joinDate;
        this.avatar = avatar;
    }

    public int getId() { return id; }
    public String getFirstname() { return firstname; }
    public String getLastname() { return lastname; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Date getJoinDate() { return joinDate; }
    public String getAvatar() { return avatar; }

    public void setId(int id) { this.id = id; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setJoinDate(Date joinDate) { this.joinDate = joinDate; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    @Override
    public String toString() {
        return "User{id=" + id + ", firstname='" + firstname + "', lastname='" + lastname + "', email='" + email + "'}";
    }
}