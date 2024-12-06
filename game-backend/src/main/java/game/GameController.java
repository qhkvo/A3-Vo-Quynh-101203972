package game;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = {"http://127.0.0.1:8081", "http://localhost:8081"})
public class GameController {

    private final Game game;

    public GameController() {
        this.game = new Game();
    }

    // Start the game and initialize decks and players
    @GetMapping("/start")
    public String startGame() {
        return game.start(); // This returns the game's start message
    }

    @GetMapping("/checkForWinnersOrProceed")
    public ResponseEntity<String> checkForWinnersOrProceed(@RequestParam(required = false) String input) {
        try {
            String response = game.checkForWinnersOrProceed(input);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error checking for winners: " + e.getMessage());
        }
    }

    @GetMapping("/A1_scenario")
    public String triggerA1Scenario() {
        try {
            return game.triggerA1Scenario();
        } catch (Exception e) {
            return "Error triggering A1 Scenario: " + e.getMessage();
        }
    }

    @GetMapping("/Winner_2")
    public String trigger2WinnerScenario() {
        try {
            return game.trigger2WinnersScenario();
        } catch (Exception e) {
            return "Error triggering A1 Scenario: " + e.getMessage();
        }
    }

    // Handle the initial resolve logic or prompt the next player
    @GetMapping("/resolveEvent")
    public ResponseEntity<String> resolveEvent() {
        try {
            String response = game.resolveEvent(null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error resolving event: " + e.getMessage());
        }
    }

    // Process the player's input
    @PostMapping("/resolveEventInput")
    public ResponseEntity<String> resolveEventInput(@RequestBody Map<String, String> request) {
        String input = request.get("input");
        try {
            String response = game.resolveEvent(input); // Pass the user's input to resolveEvent
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing input: " + e.getMessage());
        }
    }



}
