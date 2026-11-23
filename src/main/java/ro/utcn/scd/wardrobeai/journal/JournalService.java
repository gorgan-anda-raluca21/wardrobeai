package ro.utcn.scd.wardrobeai.journal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.scd.wardrobeai.notification.NotificationService;
import ro.utcn.scd.wardrobeai.outfit.Outfit;
import ro.utcn.scd.wardrobeai.outfit.OutfitItem;
import ro.utcn.scd.wardrobeai.outfit.OutfitItemRepository;
import ro.utcn.scd.wardrobeai.outfit.OutfitRepository;
import ro.utcn.scd.wardrobeai.wardrobe.ClothingItem;
import ro.utcn.scd.wardrobeai.wardrobe.ClothingItemRepository;

import java.time.LocalDate;

@Service
public class JournalService {

    private final WearLogRepository wearLogRepository;
    private final OutfitRepository outfitRepository;
    private final OutfitItemRepository outfitItemRepository;
    private final ClothingItemRepository clothingItemRepository;
    private final NotificationService notificationService;

    public JournalService(WearLogRepository wearLogRepository,
                          OutfitRepository outfitRepository,
                          OutfitItemRepository outfitItemRepository,
                          ClothingItemRepository clothingItemRepository,
                          NotificationService notificationService) {
        this.wearLogRepository = wearLogRepository;
        this.outfitRepository = outfitRepository;
        this.outfitItemRepository = outfitItemRepository;
        this.clothingItemRepository = clothingItemRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public WearLog wearOutfit(WearLogRequest request) {
        if (request.getOutfitId() == null) {
            throw new IllegalArgumentException("Lipseste outfitId");
        }

        Outfit outfit = outfitRepository.findById(request.getOutfitId())
                .orElseThrow(() -> new IllegalArgumentException("Outfitul nu exista"));

        // 1. scriem purtarea in jurnal
        WearLog log = new WearLog();
        log.setOutfit(outfit);
        log.setWornOn(LocalDate.now());
        log.setNote(request.getNote());
        WearLog saved = wearLogRepository.save(log);

        // 2. crestem contorul fiecarei piese din outfit
        for (OutfitItem outfitItem : outfitItemRepository.findByOutfitId(outfit.getId())) {
            ClothingItem item = outfitItem.getClothingItem();
            item.setTimesWorn(item.getTimesWorn() + 1);
            item.setLastWornDate(LocalDate.now());
            clothingItemRepository.save(item);
        }

        // 3. anuntam clientii conectati
        notificationService.send("Ai purtat outfitul: " + outfit.getName());

        return saved;
    }
}