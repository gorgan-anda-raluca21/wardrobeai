package ro.utcn.scd.wardrobeai.journal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/journal")
public class JournalController {

    private final JournalService journalService;
    private final WearLogRepository wearLogRepository;

    public JournalController(JournalService journalService, WearLogRepository wearLogRepository) {
        this.journalService = journalService;
        this.wearLogRepository = wearLogRepository;
    }

    // istoricul purtarilor, cele mai recente primele
    @GetMapping
    public List<WearLog> getHistory() {
        return wearLogRepository.findAllByOrderByWornOnDesc();
    }

    // poarta un outfit
    @PostMapping("/wear")
    public ResponseEntity<?> wear(@RequestBody WearLogRequest request) {
        try {
            return ResponseEntity.ok(journalService.wearOutfit(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!wearLogRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        wearLogRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}