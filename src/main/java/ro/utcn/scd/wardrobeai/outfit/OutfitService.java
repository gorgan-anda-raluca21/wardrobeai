package ro.utcn.scd.wardrobeai.outfit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.scd.wardrobeai.wardrobe.ClothingItem;
import ro.utcn.scd.wardrobeai.wardrobe.ClothingItemRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OutfitService {

    private final OutfitRepository outfitRepository;
    private final OutfitItemRepository outfitItemRepository;
    private final OccasionRepository occasionRepository;
    private final ClothingItemRepository clothingItemRepository;

    public OutfitService(OutfitRepository outfitRepository,
                         OutfitItemRepository outfitItemRepository,
                         OccasionRepository occasionRepository,
                         ClothingItemRepository clothingItemRepository) {
        this.outfitRepository = outfitRepository;
        this.outfitItemRepository = outfitItemRepository;
        this.occasionRepository = occasionRepository;
        this.clothingItemRepository = clothingItemRepository;
    }

    // @Transactional: daca ceva esil, nu ramane un outfit salvat pe jumatate
    @Transactional
    public Outfit create(OutfitRequest request) {
        Outfit outfit = new Outfit();
        outfit.setName(request.getName());
        outfit.setCreatedAt(LocalDate.now());

        if (request.getOccasionId() != null) {
            Occasion occasion = occasionRepository.findById(request.getOccasionId())
                    .orElseThrow(() -> new IllegalArgumentException("Ocazia nu exista"));
            outfit.setOccasion(occasion);
        }

        Outfit saved = outfitRepository.save(outfit);

        for (Long itemId : request.getItemIds()) {
            ClothingItem item = clothingItemRepository.findById(itemId)
                    .orElseThrow(() -> new IllegalArgumentException("Piesa " + itemId + " nu exista"));

            OutfitItem outfitItem = new OutfitItem();
            outfitItem.setOutfit(saved);
            outfitItem.setClothingItem(item);
            outfitItem.setSlot(item.getCategory() == null ? "piesa" : item.getCategory().getBodyZone());
            outfitItemRepository.save(outfitItem);
        }

        return saved;
    }

    // returneaza outfitul impreuna cu hainele lui
    public Map<String, Object> getWithItems(Long id) {
        Outfit outfit = outfitRepository.findById(id).orElse(null);
        if (outfit == null) {
            return null;
        }

        List<ClothingItem> items = new ArrayList<>();
        for (OutfitItem outfitItem : outfitItemRepository.findByOutfitId(id)) {
            items.add(outfitItem.getClothingItem());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("outfit", outfit);
        result.put("items", items);
        return result;
    }

    @Transactional
    public boolean delete(Long id) {
        if (!outfitRepository.existsById(id)) {
            return false;
        }
        outfitItemRepository.deleteAll(outfitItemRepository.findByOutfitId(id));
        outfitRepository.deleteById(id);
        return true;
    }
}