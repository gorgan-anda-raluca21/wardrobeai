package ro.utcn.scd.wardrobeai.stylist;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stylist")
public class StylistController {

    private final StylistService stylistService;

    public StylistController(StylistService stylistService) {
        this.stylistService = stylistService;
    }

    @GetMapping(value = "/suggest", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> suggest(@RequestParam String occasion,
                                     @RequestParam String weather,
                                     @RequestParam(required = false) Long userId) {
        try {
            return ResponseEntity.ok(stylistService.suggest(occasion, weather, userId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}