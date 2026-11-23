package ro.utcn.scd.wardrobeai.outfit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutfitItemRepository extends JpaRepository<OutfitItem, Long> {

    // Spring creeaza singur query-ul din numele metodei
    List<OutfitItem> findByOutfitId(Long outfitId);
}