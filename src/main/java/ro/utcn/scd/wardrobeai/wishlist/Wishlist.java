package ro.utcn.scd.wardrobeai.wishlist;

import jakarta.persistence.*;
import ro.utcn.scd.wardrobeai.user.User;

@Entity
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private String brand;
    private double estimatedPrice;
    private String link;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public double getEstimatedPrice() { return estimatedPrice; }
    public void setEstimatedPrice(double estimatedPrice) { this.estimatedPrice = estimatedPrice; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}