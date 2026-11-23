package ro.utcn.scd.wardrobeai.wishlist;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistRepository wishlistRepository;
    private final WishlistService wishlistService;

    public WishlistController(WishlistRepository wishlistRepository, WishlistService wishlistService) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public List<Wishlist> getAll() {
        return wishlistRepository.findAll();
    }

    @PostMapping
    public Wishlist create(@RequestBody Wishlist wish) {
        return wishlistRepository.save(wish);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Wishlist> update(@PathVariable Long id, @RequestBody Wishlist data) {
        return wishlistRepository.findById(id).map(wish -> {
            wish.setProductName(data.getProductName());
            wish.setBrand(data.getBrand());
            wish.setEstimatedPrice(data.getEstimatedPrice());
            wish.setLink(data.getLink());
            return ResponseEntity.ok(wishlistRepository.save(wish));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!wishlistRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        wishlistRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // endpoint complex: piesa din wishlist trece in garderoba
    @PostMapping("/{id}/bought")
    public ResponseEntity<?> bought(@PathVariable Long id, @RequestBody BoughtRequest request) {
        try {
            return ResponseEntity.ok(wishlistService.markAsBought(id, request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}