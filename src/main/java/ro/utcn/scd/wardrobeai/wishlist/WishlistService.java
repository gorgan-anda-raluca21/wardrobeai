package ro.utcn.scd.wardrobeai.wishlist;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.scd.wardrobeai.notification.NotificationService;
import ro.utcn.scd.wardrobeai.wardrobe.Category;
import ro.utcn.scd.wardrobeai.wardrobe.CategoryRepository;
import ro.utcn.scd.wardrobeai.wardrobe.ClothingItem;
import ro.utcn.scd.wardrobeai.wardrobe.ClothingItemRepository;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ClothingItemRepository clothingItemRepository;
    private final CategoryRepository categoryRepository;
    private final NotificationService notificationService;

    public WishlistService(WishlistRepository wishlistRepository,
                           ClothingItemRepository clothingItemRepository,
                           CategoryRepository categoryRepository,
                           NotificationService notificationService) {
        this.wishlistRepository = wishlistRepository;
        this.clothingItemRepository = clothingItemRepository;
        this.categoryRepository = categoryRepository;
        this.notificationService = notificationService;
    }

    // Am cumparat piesa: o mutam din wishlist in garderoba
    @Transactional
    public ClothingItem markAsBought(Long wishId, BoughtRequest request) {
        Wishlist wish = wishlistRepository.findById(wishId)
                .orElseThrow(() -> new IllegalArgumentException("Piesa nu exista in wishlist"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria nu exista"));

        // 1. cream piesa noua in garderoba, cu datele din wishlist
        ClothingItem item = new ClothingItem();
        item.setName(wish.getProductName());
        item.setBrand(wish.getBrand());
        item.setPrice(wish.getEstimatedPrice());
        item.setColor(request.getColor());
        item.setSize(request.getSize());
        item.setCategory(category);
        item.setTimesWorn(0);
        ClothingItem saved = clothingItemRepository.save(item);

        // 2. o stergem din wishlist
        wishlistRepository.delete(wish);

        // 3. anuntam clientii conectati
        notificationService.send("Piesa noua in garderoba: " + saved.getName());

        return saved;
    }
}