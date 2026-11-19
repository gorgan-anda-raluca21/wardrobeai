package ro.utcn.scd.wardrobeai.model;

import jakarta.persistence.*;

@Entity
public class OutfitItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String slot;

    @ManyToOne
    @JoinColumn(name = "outfit_id")
    private Outfit outfit;

    @ManyToOne
    @JoinColumn(name = "clothing_item_id")
    private ClothingItem clothingItem;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSlot() { return slot; }
    public void setSlot(String slot) { this.slot = slot; }

    public Outfit getOutfit() { return outfit; }
    public void setOutfit(Outfit outfit) { this.outfit = outfit; }

    public ClothingItem getClothingItem() { return clothingItem; }
    public void setClothingItem(ClothingItem clothingItem) { this.clothingItem = clothingItem; }
}