package ro.utcn.scd.wardrobeai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.utcn.scd.wardrobeai.model.Occasion;

public interface OccasionRepository extends JpaRepository<Occasion, Long> {
}