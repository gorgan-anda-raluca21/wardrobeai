package ro.utcn.scd.wardrobeai.journal;

public class WearLogRequest {

    private Long outfitId;
    private String note;

    public Long getOutfitId() {
        return outfitId;
    }

    public void setOutfitId(Long outfitId) {
        this.outfitId = outfitId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}