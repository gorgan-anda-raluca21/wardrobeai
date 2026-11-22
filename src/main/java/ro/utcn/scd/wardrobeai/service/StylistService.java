package ro.utcn.scd.wardrobeai.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ro.utcn.scd.wardrobeai.wardrobe.ClothingItem;
import ro.utcn.scd.wardrobeai.wardrobe.ClothingItemRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StylistService {

    private static final String API_URL = "http://localhost:11434/api/chat";
    private static final String MODEL = "llama3.2";

    private final ClothingItemRepository itemRepository;

    public StylistService(ClothingItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> suggest(String occasion, String weather) {

        // 1. Luam garderoba din baza de date
        List<ClothingItem> items = itemRepository.findAll();
        if (items.isEmpty()) {
            throw new IllegalStateException("Garderoba este goala. Adauga haine mai intai.");
        }

        // 2. Construim textul (prompt-ul) pentru model
        StringBuilder wardrobe = new StringBuilder();
        for (ClothingItem item : items) {
            wardrobe.append("id=").append(item.getId())
                    .append(", ").append(item.getName())
                    .append(", culoare: ").append(item.getColor())
                    .append(", brand: ").append(item.getBrand())
                    .append("\n");
        }

        String prompt = "Esti un stilist personal. Garderoba utilizatorului:\n"
                + wardrobe
                + "\nOcazie: " + occasion + "\nVreme: " + weather
                + "\n\nAlege o tinuta DOAR din hainele de mai sus. "
                + "Raspunde EXACT in formatul:\n"
                + "IDS: id1,id2,id3\n"
                + "EXPLICATIE: o explicatie scurta in romana";

        // 3. Apelam modelul local (Ollama)
        Map<String, Object> body = Map.of(
                "model", MODEL,
                "stream", false,
                "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        Map<String, Object> response = RestClient.create()
                .post()
                .uri(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(Map.class);

        Map<String, Object> message = (Map<String, Object>) response.get("message");
        String text = (String) message.get("content");

        // 4. Citim raspunsul: linia IDS si EXPLICATIE
        String idsPart = "";
        String explanation = text;
        for (String line : text.split("\n")) {
            if (line.startsWith("IDS:")) {
                idsPart = line.substring(4).trim();
            }
        }
        int pos = text.indexOf("EXPLICATIE:");
        if (pos >= 0) {
            explanation = text.substring(pos + 11).trim();
        }

        // 5. Validam: pastram doar id-urile care exista in baza de date
        List<ClothingItem> chosen = new ArrayList<>();
        for (String part : idsPart.split(",")) {
            try {
                long id = Long.parseLong(part.trim());
                for (ClothingItem item : items) {
                    if (item.getId() == id) {
                        chosen.add(item);
                    }
                }
            } catch (NumberFormatException e) {
                // ignoram ce nu e numar
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("items", chosen);
        result.put("explanation", explanation);
        return result;
    }
}