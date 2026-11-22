package ro.utcn.scd.wardrobeai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.utcn.scd.wardrobeai.wardrobe.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}