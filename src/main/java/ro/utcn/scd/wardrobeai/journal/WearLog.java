package ro.utcn.scd.wardrobeai.journal;

import jakarta.persistence.*;
import ro.utcn.scd.wardrobeai.user.User;
import ro.utcn.scd.wardrobeai.outfit.Outfit;

import java.time.LocalDate;

@Entity
public class WearLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate wornOn;
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "outfit_id")
    private Outfit outfit;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getWornOn() { return wornOn; }
    public void setWornOn(LocalDate wornOn) { this.wornOn = wornOn; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Outfit getOutfit() { return outfit; }
    public void setOutfit(Outfit outfit) { this.outfit = outfit; }
}