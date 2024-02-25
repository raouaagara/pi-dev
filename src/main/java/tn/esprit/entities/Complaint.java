package tn.esprit.entities;

import java.io.InputStream;
import java.util.Date;

public class Complaint {

    private int complaintId;
    private String title;
    private String description;
    private String category;
    private String location;
    private String status;
    //private final InputStream image;
  //  private Date datePosted;
    private String user;
    //private int complaintCreatorScore;


    public Complaint(String title, String description, String category, String location,String user){
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.status = status;
        //this.datePosted = datePosted;
        this.user = user;
    }

  /*  public Complaint(int complaintId,String title, String description, String category, String location,
                     String status, InputStream image, Date datePosted, String user, int complaintCreatorScore) {
        this.complaintId= complaintId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.status = status;
       // this.image = image;
        this.datePosted = datePosted;
        this.user = user;
        this.complaintCreatorScore = complaintCreatorScore;
    }*/
  public Complaint(int complaintId,String title, String description, String category, String location, String status, String user) {
      this.complaintId = complaintId;
      this.title = title;
      this.description = description;
      this.category = category;
      this.location = location;
      this.status = status;
     // this.datePosted = datePosted;
      this.user = user;
  }

    public Complaint(String title, String description, String category, String location, String status, String user) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.status = status;
        this.user = user;
    }



    public int getComplaintId() {
       return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   /* public InputStream getImage() {
        return image;
    }
*/
   /* public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }
*/
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    /*public int getComplaintCreatorScore() {
        return complaintCreatorScore;
    }

    public void setComplaintCreatorScore(int complaintCreatorScore) {
        this.complaintCreatorScore = complaintCreatorScore;
    }
*/
    @Override
    public String toString() {
        return "Complaint{" +
                "complaintId=" + complaintId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                ", status='" + status + '\'' +
             //   ", image=" + image +
             //   ", datePosted=" + datePosted +
                ", user='" + user + '\'' +
              //  ", complaintCreatorScore=" + complaintCreatorScore +
                '}';
    }
}
