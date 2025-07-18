package smart.smart_contract.entity;




import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="CONTACT")
public class Contact {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int cId;
    private String fullName;
   
    private String work;
    private String email;
    private String phone;
    private String image;

    @ManyToOne
    private User user;

    public int getcId() {
        return cId;
    }
    public void setcId(int cId) {
        this.cId = cId;
    }
  public String getFullName() {
    return fullName;
}
public void setFullName(String fullName) {
    this.fullName = fullName;
}

    public Contact() {
    }
  
    public String getWork() {
        return work;
    }
    public void setWork(String work) {
        this.work = work;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
 
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

   @Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Contact{");
    sb.append("cId=").append(cId);
    sb.append(", fullname=").append(fullName);
    sb.append(", work=").append(work);
    sb.append(", email=").append(email);
    sb.append(", phone=").append(phone);
    sb.append(", image=").append(image);
    sb.append(", userId=").append(user != null ? user.getId() : "null");
    sb.append('}');
    return sb.toString();
}
public boolean equals(Object obj){
    return this.cId==((Contact)obj).getcId();
}
    
}
