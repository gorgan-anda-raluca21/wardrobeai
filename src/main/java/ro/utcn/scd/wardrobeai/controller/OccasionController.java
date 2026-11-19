package ro.utcn.scd.wardrobeai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.utcn.scd.wardrobeai.model.Occasion;
import ro.utcn.scd.wardrobeai.repository.OccasionRepository;

import java.util.List;

@RestController
@RequestMapping("/api/occasions")
public class OccasionController {

    private final OccasionRepository repository;

    public OccasionController(OccasionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Occasion> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Occasion> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Occasion create(@RequestBody Occasion occasion) {
        return repository.save(occasion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Occasion> update(@PathVariable Long id, @RequestBody Occasion data) {
        return repository.findById(id).map(occasion -> {
            occasion.setName(data.getName());
            occasion.setDescription(data.getDescription());
            occasion.setDressCode(data.getDressCode());
            return ResponseEntity.ok(repository.save(occasion));
        }).orElse(ResponseEntity.notFound().build());
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