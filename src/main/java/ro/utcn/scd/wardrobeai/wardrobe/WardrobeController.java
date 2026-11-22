package ro.utcn.scd.wardrobeai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.utcn.scd.wardrobeai.wardrobe.ClothingItem;
import ro.utcn.scd.wardrobeai.repository.ClothingItemRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wardrobe")
public class WardrobeController {

    private final ClothingItemRepository repository;

    public WardrobeController(ClothingItemRepository repository) {
        this.repository = repository;
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

    // Complex 4: "poarta piesa" - creste contorul si seteaza data
    @PostMapping("/wear/{id}")
    public ResponseEntity<ClothingItem> wear(@PathVariable Long id) {
        return repository.findById(id).map(item -> {
            item.setTimesWorn(item.getTimesWorn() + 1);
            item.setLastWornDate(LocalDate.now());
            return ResponseEntity.ok(repository.save(item));
        }).orElse(ResponseEntity.notFound().build());
    }
}