package ro.utcn.scd.wardrobeai.notification;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ro.utcn.scd.wardrobeai.wardrobe.ClothingItem;
import ro.utcn.scd.wardrobeai.wardrobe.ClothingItemRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {

    private final ClothingItemRepository repository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(ClothingItemRepository repository,
                               SimpMessagingTemplate messagingTemplate) {
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
    }

    // trimite un mesaj tuturor clientilor abonati la canal
    public void send(String message) {
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }

    // ruleaza singur la 30 de secunde (pentru demo; in realitate ar fi o data pe zi)
    @Scheduled(initialDelay = 10000, fixedRate = 30000)
    public void checkUnwornItems() {
        LocalDate limit = LocalDate.now().minusDays(30);
        List<ClothingItem> items = repository.findNotWornSince(limit);

        if (items.isEmpty()) {
            return;
        }

        StringBuilder names = new StringBuilder();
        for (ClothingItem item : items) {
            names.append(item.getName()).append(", ");
        }

        send("Ai " + items.size() + " piese nepurtate de 30 de zile: " + names);
    }
}