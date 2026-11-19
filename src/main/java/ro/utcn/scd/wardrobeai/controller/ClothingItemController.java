package ro.utcn.scd.wardrobeai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.utcn.scd.wardrobeai.model.ClothingItem;
import ro.utcn.scd.wardrobeai.repository.ClothingItemRepository;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ClothingItemController {

    private final ClothingItemRepository repository;

    public ClothingItemController(ClothingItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<ClothingItem> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClothingItem> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ClothingItem create(@RequestBody ClothingItem item) {
        return repository.save(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClothingItem> update(@PathVariable Long id, @RequestBody ClothingItem item) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        item.setId(id);
        return ResponseEntity.ok(repository.save(item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}