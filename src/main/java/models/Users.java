package models;

import java.util.Date;

public class Users {
    private int id;
    private String username;
    private String email;
    private String password;
    private Date joinDate;

    public Users(int id, String username, String mail, String password, String date) {
    }

    public Users(int id, String username, String email, String password, Date joinDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.joinDate = joinDate;
    }

    public Users(String username, String email, String password, Date joinDate) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.joinDate = joinDate;
    }

    public Users() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", joinDate=" + joinDate +
                '}';
    }
}
