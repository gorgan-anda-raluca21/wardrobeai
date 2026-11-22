package ro.utcn.scd.wardrobeai.wardrobe;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.utcn.scd.wardrobeai.notification.NotificationService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wardrobe")
public class WardrobeController {

    private final ClothingItemRepository repository;
    private final CategoryRepository categoryRepository;
    private final NotificationService notificationService;

    public WardrobeController(ClothingItemRepository repository,
                              CategoryRepository categoryRepository,
                              NotificationService notificationService) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.notificationService = notificationService;
    }

    // Complex 1: cautare cu filtre (query propriu)
    @GetMapping("/search")
    public List<ClothingItem> search(@RequestParam(required = false) String color,
                                     @RequestParam(required = false) String brand,
                                     @RequestParam(required = false) Double maxPrice) {
        return repository.search(color, brand, maxPrice);
    }

    // Complex 2: piese nepurtate de X zile (query propriu)
    @GetMapping("/unworn")
    public List<ClothingItem> unworn(@RequestParam(defaultValue = "30") int days) {
        LocalDate limit = LocalDate.now().minusDays(days);
        return repository.findNotWornSince(limit);
    }

    // Complex 3: statistici despre garderoba
    @GetMapping("/stats")
    public Map<String, Object> stats() {
        List<ClothingItem> items = repository.findAll();
        double totalValue = 0;
        int neverWorn = 0;
        ClothingItem mostWorn = null;

        for (ClothingItem item : items) {
            totalValue += item.getPrice();
            if (item.getTimesWorn() == 0) {
                neverWorn++;
            }
            if (mostWorn == null || item.getTimesWorn() > mostWorn.getTimesWorn()) {
                mostWorn = item;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalItems", items.size());
        result.put("totalValue", totalValue);
        result.put("neverWorn", neverWorn);
        result.put("mostWorn", mostWorn == null ? null : mostWorn.getName());
        return result;
    }

    // Complex 4: "poarta piesa" - creste contorul, seteaza data si trimite notificare
    @PostMapping("/wear/{id}")
    public ResponseEntity<ClothingItem> wear(@PathVariable Long id) {
        return repository.findById(id).map(item -> {
            item.setTimesWorn(item.getTimesWorn() + 1);
            item.setLastWornDate(LocalDate.now());
            ClothingItem saved = repository.save(item);
            notificationService.send("Ai purtat: " + saved.getName()
                    + " (de " + saved.getTimesWorn() + " ori)");
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Atribuie o categorie unei haine
    @PutMapping("/{itemId}/category/{categoryId}")
    public ResponseEntity<ClothingItem> assignCategory(@PathVariable Long itemId,
                                                       @PathVariable Long categoryId) {
        ClothingItem item = repository.findById(itemId).orElse(null);
        Category category = categoryRepository.findById(categoryId).orElse(null);

        if (item == null || category == null) {
            return ResponseEntity.notFound().build();
        }

        item.setCategory(category);
        return ResponseEntity.ok(repository.save(item));
    }
}