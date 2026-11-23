package ro.utcn.scd.wardrobeai.journal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WearLogRepository extends JpaRepository<WearLog, Long> {

    // cele mai recente purtari primele
    List<WearLog> findAllByOrderByWornOnDesc();
}