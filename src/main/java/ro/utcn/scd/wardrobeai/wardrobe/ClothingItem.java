package ro.utcn.scd.wardrobeai.wardrobe;

import jakarta.persistence.*;
import ro.utcn.scd.wardrobeai.model.User;

import java.time.LocalDate;

@Entity
public class ClothingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;
    private String brand;
    private String size;
    private double price;
    private String imageUrl;
    private int timesWorn;
    private LocalDate lastWornDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getTimesWorn() { return timesWorn; }
    public void setTimesWorn(int timesWorn) { this.timesWorn = timesWorn; }

    public LocalDate getLastWornDate() { return lastWornDate; }
    public void setLastWornDate(LocalDate lastWornDate) { this.lastWornDate = lastWornDate; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}