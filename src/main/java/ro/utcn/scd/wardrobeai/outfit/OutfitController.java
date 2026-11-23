package ro.utcn.scd.wardrobeai.outfit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/outfits")
public class OutfitController {

    private final OutfitService outfitService;
    private final OutfitRepository outfitRepository;

    public OutfitController(OutfitService outfitService, OutfitRepository outfitRepository) {
        this.outfitService = outfitService;
        this.outfitRepository = outfitRepository;
    }

    @GetMapping
    public List<Outfit> getAll() {
        return outfitRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        Map<String, Object> result = outfitService.getWithItems(id);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody OutfitRequest request) {
        try {
            return ResponseEntity.ok(outfitService.create(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!outfitService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}