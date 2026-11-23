package ro.utcn.scd.wardrobeai.outfit;

import java.util.List;

public class OutfitRequest {

    private String name;
    private Long occasionId;
    private List<Long> itemIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOccasionId() {
        return occasionId;
    }

    public void setOccasionId(Long occasionId) {
        this.occasionId = occasionId;
    }

    public List<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }
}