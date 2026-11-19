package ro.utcn.scd.wardrobeai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.utcn.scd.wardrobeai.model.ClothingItem;

public interface ClothingItemRepository extends JpaRepository<ClothingItem, Long> {
}