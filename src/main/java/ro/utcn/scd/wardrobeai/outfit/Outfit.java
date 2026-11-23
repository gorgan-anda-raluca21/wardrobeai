package ro.utcn.scd.wardrobeai.outfit;

import jakarta.persistence.*;
import ro.utcn.scd.wardrobeai.model.User;

import java.time.LocalDate;

@Entity
public class Outfit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "occasion_id")
    private Occasion occasion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Occasion getOccasion() { return occasion; }
    public void setOccasion(Occasion occasion) { this.occasion = occasion; }
}