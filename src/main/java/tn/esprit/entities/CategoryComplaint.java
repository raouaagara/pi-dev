package tn.esprit.entities;

public class CategoryComplaint {
    private int idCateg;
    private String nameCateg;
    private int userid;

    public CategoryComplaint(int idCateg, String nameCateg, int userid) {
        this.idCateg = idCateg;
        this.nameCateg = nameCateg;
        this.userid = userid;
    }

    public int getIdCateg() {
        return idCateg;
    }

    public void setIdCateg(int idCateg) {
        this.idCateg = idCateg;
    }

    public String getNameCateg() {
        return nameCateg;
    }

    public void setNameCateg(String nameCateg) {
        this.nameCateg = nameCateg;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
    @Override
    public String toString() {
        return "CategoryComplaint{" +
                "idCateg=" + idCateg +
                ", nameCateg='" + nameCateg + '\'' +
                ", userid=" + userid +
                '}';
    }
}
