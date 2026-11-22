package ro.utcn.scd.wardrobeai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.utcn.scd.wardrobeai.wardrobe.ClothingItem;

import java.time.LocalDate;
import java.util.List;

public interface ClothingItemRepository extends JpaRepository<ClothingItem, Long> {

    // Query 1: cautare cu filtre optionale (daca un filtru e null, se ignora)
    @Query("SELECT c FROM ClothingItem c WHERE "
            + "(:color IS NULL OR LOWER(c.color) = LOWER(:color)) AND "
            + "(:brand IS NULL OR LOWER(c.brand) = LOWER(:brand)) AND "
            + "(:maxPrice IS NULL OR c.price <= :maxPrice)")
    List<ClothingItem> search(@Param("color") String color,
                              @Param("brand") String brand,
                              @Param("maxPrice") Double maxPrice);

    // Query 2: piese nepurtate niciodata sau purtate inainte de o anumita data
    @Query("SELECT c FROM ClothingItem c WHERE c.lastWornDate IS NULL OR c.lastWornDate < :limit")
    List<ClothingItem> findNotWornSince(@Param("limit") LocalDate limit);
}