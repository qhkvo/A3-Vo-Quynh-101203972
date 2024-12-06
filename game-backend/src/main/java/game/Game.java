package game;

import java.io.PrintWriter;
import java.util.*;

public class Game {
    private enum GameState {
        IDLE,
        PROMPTING_SPONSORSHIP,
        SETTING_UP_QUEST,
        STARTING_QUEST,
        TRIMMING_SPONSOR,
        TRIMMING_ONE_HAND,
        TRIMMING_EVENT,
        PROMPTING_PARTICIPATE,
        BUILDING_ATTACK_CARD,
        END_QUEST
    }

    private Player p1, p2, p3, p4;
    private GameState currentGameState;
    private List<Player> players;
    private Deck adventureDeck;
    private Deck eventDeck;
     //private int currentPlayerIndex;
    private int initialPlayerIndex;
    private List<Card> discardAPile;
    private List<Card> discardEPile;
    private int sponsorIndex;
    private List<Player> withdrawnParticipants;
    private int stageCount;
    private List<List<Card>> totalStagesCards;
    private List<Player> winners;
    private List<String> messages;
    private boolean isFirstTurn;  // Track if it's the first turn or not]
    private EventCard eventCard1;
    private int playerToAnswerIndex;
    private int currentStage = 0; // Tracks the current stage of the quest
    private int trimmingIndex = 0;
    private int currentPlayerIndex = 0;
    private Player playerToTrim = new Player();
    private Player playerBuildingAttack = new Player();
    private List<Player> staticPlayersToTrim = new ArrayList<>();
    private int currentParticipantIndex = 0;
    private List<Player> eligibleParticipants;
    private List<Card> weaponCards;
    private List<Card> chosenCards = new ArrayList<>();
    private boolean hasFinishedPromptParticipation = false;
    private List<Player> successfulParticipants = new ArrayList<>();
    private List<Player> originalEligibleParticipantsSize ;
    private int promptCount;
    private boolean lastParticipant = false;
    private List<Player> updatedEligibleList = new ArrayList();
    private Player sponsor = new Player();

    // Constructor to initialize players and set up decks
    public Game() {
        currentGameState = GameState.IDLE;
        players = new ArrayList<>();
        discardAPile = new ArrayList<>();
        discardEPile = new ArrayList<>();
        withdrawnParticipants = new ArrayList<>();
        stageCount = 0;
        currentStage = 0;
        totalStagesCards = new ArrayList<>();
        winners = new ArrayList<>();
        isFirstTurn = true;
        messages = new ArrayList<>();
        playerToAnswerIndex = -1; // Tracks the player being prompted for sponsorship
        promptCount = 0;

        // Create 4 players
        for (int i = 0; i < 4; i++){
            players.add(new Player());
        }

        // Initialize the decks
        setUpDecks();
    }

    public String start() {
        setUpDecks();
        distributeCards();
        messages.add("Game started successfully! Players have been dealt their cards.");
        playTurn();
        resolveEvent(null);

        return String.join("\n", messages);
    }

    public String triggerA1Scenario() {
        setUpDecks();
        List<Card> drawnACards = Arrays.asList(new Card("F30",30), new Card("S10", 10),
                new Card("B15", 15), new Card("F10", 10),
                new Card("L20", 20), new Card("L20", 20),
                new Card("B15", 15), new Card("S10", 10),
                new Card("F30", 30), new Card("L20", 20)
        );

        List<Card> drawnECards = List.of(new Card("Q4", 4));

        getAdventureDeck().addOnTop(drawnACards);
        getEventDeck().addOnTop(drawnECards);

        List<Player> players = getPlayers();
        p1 = players.get(0);
        p2 = players.get(1);
        p3 = players.get(2);
        p4 = players.get(3);

        p1.removeHand();
        p2.removeHand();
        p3.removeHand();
        p4.removeHand();

        p1.addCardToHand( new Card("F5", 5));
        p1.addCardToHand( new Card("F5", 5));
        p1.addCardToHand( new Card("F15", 15));
        p1.addCardToHand( new Card("F15",15));
        p1.addCardToHand( new Card("D5", 5));
        p1.addCardToHand( new Card("S10", 10));
        p1.addCardToHand( new Card("S10",  10));
        p1.addCardToHand( new Card("H10",  10));
        p1.addCardToHand( new Card("H10",  10));
        p1.addCardToHand( new Card("B15", 15));
        p1.addCardToHand( new Card("B15", 15));
        p1.addCardToHand( new Card("L20",  20));

        p2.addCardToHand(new Card("F5", 5));
        p2.addCardToHand(new Card("F5", 5));
        p2.addCardToHand(new Card("F15",15));
        p2.addCardToHand(new Card("F15",15));
        p2.addCardToHand(new Card("F40",40));
        p2.addCardToHand(new Card("D5",  5));
        p2.addCardToHand(new Card("S10", 10));
        p2.addCardToHand(new Card("H10", 10));
        p2.addCardToHand(new Card("H10", 10));
        p2.addCardToHand(new Card("B15", 15));
        p2.addCardToHand(new Card("B15", 15));
        p2.addCardToHand(new Card("L20", 20));

        p3.addCardToHand(new Card("F5", 5));
        p3.addCardToHand(new Card("F5", 5));
        p3.addCardToHand(new Card("F5",5));
        p3.addCardToHand(new Card("F15",15));
        p3.addCardToHand(new Card("D5",  5));
        p3.addCardToHand(new Card("S10", 10));
        p3.addCardToHand(new Card("S10", 10));
        p3.addCardToHand(new Card("S10", 10));
        p3.addCardToHand(new Card("H10", 10));
        p3.addCardToHand(new Card("H10", 10));
        p3.addCardToHand(new Card("B15", 15));
        p3.addCardToHand(new Card("L20", 20));

        p4.addCardToHand(new Card("F5", 5));
        p4.addCardToHand(new Card("F15", 15));
        p4.addCardToHand(new Card("F15",15));
        p4.addCardToHand(new Card("F40",40));
        p4.addCardToHand(new Card("D5",  5));
        p4.addCardToHand(new Card("D5", 5));
        p4.addCardToHand(new Card("S10", 10));
        p4.addCardToHand(new Card("H10", 10));
        p4.addCardToHand(new Card("H10", 10));
        p4.addCardToHand(new Card("B15", 15));
        p4.addCardToHand(new Card("L20", 20));
        p4.addCardToHand(new Card("E30", 30));

        play();
        return String.join("\n", messages);
    }

    public String trigger2WinnersScenario() {
        setUpDecks();
        List<Card> drawnACards = Arrays.asList(
                new Card("F5",5), new Card("F40", 40),
                new Card("F10", 10), new Card("F10", 10),
                new Card("F30", 30), new Card("F30", 30),
                new Card("F15", 15), new Card("F15", 15),
                new Card("F20", 20), new Card("F5",5),
                new Card("F10", 10), new Card("F15", 15),
                new Card("F15", 15), new Card("F20", 20),
                new Card("F20", 20), new Card("F20", 20),
                new Card("F20", 20), new Card("F25", 25),
                new Card("F25", 25), new Card("F30", 30),
                new Card("D5", 5), new Card("D5", 5),
                new Card("F15", 15), new Card("F15", 15),
                new Card("F25", 25), new Card("F25", 25),
                new Card("F20", 20), new Card("F20", 20),
                new Card("F25", 25), new Card("F30", 30),
                new Card("S10", 10), new Card("B15", 15),
                new Card("B15", 15), new Card("L20", 20)
        );

        List<Card> drawnECards = List.of(new Card("Q4", 4), new Card("Q3", 3));

        getAdventureDeck().addOnTop(drawnACards);
        getEventDeck().addOnTop(drawnECards);

        List<Player> players = getPlayers();
        p1 = players.get(0);
        p2 = players.get(1);
        p3 = players.get(2);
        p4 = players.get(3);

        p1.removeHand();
        p2.removeHand();
        p3.removeHand();
        p4.removeHand();

        p1.addCardToHand( new Card("F5", 5));
        p1.addCardToHand( new Card("F5", 5));
        p1.addCardToHand( new Card("F10",10));
        p1.addCardToHand( new Card("F10",10));
        p1.addCardToHand( new Card("F15", 15));
        p1.addCardToHand( new Card("F15", 15));
        p1.addCardToHand( new Card("D5", 5));
        p1.addCardToHand( new Card("H10", 10));
        p1.addCardToHand( new Card("H10", 10));
        p1.addCardToHand( new Card("B15", 15));
        p1.addCardToHand( new Card("B15", 15));
        p1.addCardToHand( new Card("L20", 20));

        p2.addCardToHand(new Card("F40", 40));
        p2.addCardToHand(new Card("F50", 50));
        p2.addCardToHand(new Card("S10", 10));
        p2.addCardToHand(new Card("S10", 10));
        p2.addCardToHand(new Card("S10", 10));
        p2.addCardToHand(new Card("H10", 10));
        p2.addCardToHand(new Card("H10", 10));
        p2.addCardToHand(new Card("B15", 15));
        p2.addCardToHand(new Card("B15", 15));
        p2.addCardToHand(new Card("L20",20));
        p2.addCardToHand(new Card("L20", 20));
        p2.addCardToHand(new Card("E30",30));

        p3.addCardToHand(new Card("F5", 5));
        p3.addCardToHand(new Card("F5", 5));
        p3.addCardToHand(new Card("F5", 5));
        p3.addCardToHand(new Card("F5", 5));
        p3.addCardToHand(new Card("D5",  5));
        p3.addCardToHand(new Card("D5", 5));
        p3.addCardToHand(new Card("D5", 5));
        p3.addCardToHand(new Card("H10", 10));
        p3.addCardToHand(new Card("H10", 10));
        p3.addCardToHand(new Card("H10", 10));
        p3.addCardToHand(new Card("H10", 10));
        p3.addCardToHand(new Card("H10", 10));

        p4.addCardToHand(new Card("F50", 50));
        p4.addCardToHand(new Card("F70", 70));
        p4.addCardToHand(new Card("S10", 10));
        p4.addCardToHand(new Card("S10", 10));
        p4.addCardToHand(new Card("S10", 10));
        p4.addCardToHand(new Card("H10", 10));
        p4.addCardToHand(new Card("H10", 10));
        p4.addCardToHand(new Card("B15", 15));
        p4.addCardToHand(new Card("B15", 15));
        p4.addCardToHand(new Card("L20", 20));
        p4.addCardToHand(new Card("L20",20));
        p4.addCardToHand(new Card("E30",30));

        play();
        return String.join("\n", messages);
    }

    public String trigger1WinnerScenario() {
        setUpDecks();
        List<Card> drawnACards = Arrays.asList(
                new Card("F5",5), new Card("F10", 10),
                new Card("F20", 20), new Card("F15", 15),
                new Card("F5", 5), new Card("F25", 25),
                new Card("F5", 5), new Card("F10", 10),
                new Card("F20", 20), new Card("F5",5),
                new Card("F10", 10), new Card("F20", 20),
                new Card("F25", 25), new Card("F25", 25),
                new Card("H10", 10), new Card("S10", 10),
                new Card("B15", 15), new Card("F40", 40),
                new Card("D5", 5), new Card("D5", 5),
                new Card("F30", 30), new Card("F25", 25),
                new Card("B15", 15), new Card("H10", 10),
                new Card("F50", 50), new Card("S10", 10),
                new Card("S10", 10), new Card("F40", 40),
                new Card("F50", 50), new Card("H10", 10),
                new Card("H10", 10), new Card("H10", 10),
                new Card("S10", 10), new Card("S10", 10),
                new Card("S10", 10), new Card("S10", 10),
                new Card("F35", 35)
        );

        List<Card> drawnECards = List.of(new Card("Q4", 4), new Card("Plague", 0), new Card("Prosperity", 0), new Card("Queen's favor", 0));

        getAdventureDeck().addOnTop(drawnACards);
        getEventDeck().addOnTop(drawnECards);

        List<Player> players = getPlayers();
        p1 = players.get(0);
        p2 = players.get(1);
        p3 = players.get(2);
        p4 = players.get(3);

        p1.removeHand();
        p2.removeHand();
        p3.removeHand();
        p4.removeHand();

        p1.addCardToHand( new Card("F5", 5));
        p1.addCardToHand( new Card("F5", 5));
        p1.addCardToHand( new Card("F10",10));
        p1.addCardToHand( new Card("F10",10));
        p1.addCardToHand( new Card("F15", 15));
        p1.addCardToHand( new Card("F15", 15));
        p1.addCardToHand( new Card("F20", 20));
        p1.addCardToHand( new Card("F20", 20));
        p1.addCardToHand( new Card("D5", 5));
        p1.addCardToHand( new Card("D5", 5));
        p1.addCardToHand( new Card("D5", 5));
        p1.addCardToHand( new Card("D5", 5));

        p2.addCardToHand(new Card("F25", 25));
        p2.addCardToHand(new Card("F30", 30));
        p2.addCardToHand(new Card("S10", 10));
        p2.addCardToHand(new Card("S10", 10));
        p2.addCardToHand(new Card("S10", 10));
        p2.addCardToHand(new Card("H10", 10));
        p2.addCardToHand(new Card("H10", 10));
        p2.addCardToHand(new Card("B15", 15));
        p2.addCardToHand(new Card("B15", 15));
        p2.addCardToHand(new Card("L20",20));
        p2.addCardToHand(new Card("L20", 20));
        p2.addCardToHand(new Card("E30",30));

        p3.addCardToHand(new Card("F25", 25));
        p3.addCardToHand(new Card("F30", 30));
        p3.addCardToHand(new Card("S10", 10));
        p3.addCardToHand(new Card("S10", 10));
        p3.addCardToHand(new Card("S10", 10));
        p3.addCardToHand(new Card("H10", 10));
        p3.addCardToHand(new Card("H10", 10));
        p3.addCardToHand(new Card("B15", 15));
        p3.addCardToHand(new Card("B15", 15));
        p3.addCardToHand(new Card("L20", 20));
        p3.addCardToHand(new Card("L20", 20));
        p3.addCardToHand(new Card("E30",30));

        p4.addCardToHand(new Card("F25",25));
        p4.addCardToHand(new Card("F30", 30));
        p4.addCardToHand(new Card("F70", 70));
        p4.addCardToHand(new Card("S10", 10));
        p4.addCardToHand(new Card("S10", 10));
        p4.addCardToHand(new Card("S10", 10));
        p4.addCardToHand(new Card("H10", 10));
        p4.addCardToHand(new Card("H10", 10));
        p4.addCardToHand(new Card("B15", 15));
        p4.addCardToHand(new Card("B15", 15));
        p4.addCardToHand(new Card("L20", 20));
        p4.addCardToHand(new Card("L20",20));

        play();
        return String.join("\n", messages);
    }

    public String trigger0WinnersScenario() {
        setUpDecks();
        List<Card> drawnACards = Arrays.asList(
                new Card("F5",5), new Card("F15", 15),
                new Card("F10", 10), new Card("F5",5),
                new Card("F10", 10), new Card("F15", 15),
                new Card("D5", 5), new Card("D5", 5),
                new Card("D5", 5), new Card("D5", 5),
                new Card("S10", 10), new Card("S10", 10),
                new Card("S10", 10), new Card("H10", 10),
                new Card("H10", 10), new Card("H10", 10),
                new Card("H10", 10)

        );

        List<Card> drawnECards = List.of(new Card("Q2", 2));

        getAdventureDeck().addOnTop(drawnACards);
        getEventDeck().addOnTop(drawnECards);

        List<Player> players = getPlayers();
        p1 = players.get(0);
        p2 = players.get(1);
        p3 = players.get(2);
        p4 = players.get(3);

        p1.removeHand();
        p2.removeHand();
        p3.removeHand();
        p4.removeHand();

        p1.addCardToHand( new Card("F50", 50));
        p1.addCardToHand( new Card("F70", 70));
        p1.addCardToHand( new Card("D5", 5));
        p1.addCardToHand( new Card("D5", 5));
        p1.addCardToHand(new Card("S10", 10));
        p1.addCardToHand(new Card("S10", 10));
        p1.addCardToHand( new Card("H10", 10));
        p1.addCardToHand( new Card("H10", 10));
        p1.addCardToHand( new Card("B15", 15));
        p1.addCardToHand( new Card("B15", 15));
        p1.addCardToHand( new Card("L20", 20));
        p1.addCardToHand( new Card("L20", 20));

        p2.addCardToHand(new Card("F5", 5));
        p2.addCardToHand(new Card("F5", 5));
        p2.addCardToHand(new Card("F10", 10));
        p2.addCardToHand(new Card("F15", 15));
        p2.addCardToHand(new Card("F15", 15));
        p2.addCardToHand(new Card("F20", 20));
        p2.addCardToHand(new Card("F20", 20));
        p2.addCardToHand(new Card("F25", 25));
        p2.addCardToHand(new Card("F30", 30));
        p2.addCardToHand(new Card("F30", 30));
        p2.addCardToHand(new Card("F40", 40));
        p2.addCardToHand(new Card("E30",30));

        p3.addCardToHand(new Card("F5", 5));
        p3.addCardToHand(new Card("F5", 5));
        p3.addCardToHand(new Card("F10", 10));
        p3.addCardToHand(new Card("F15", 15));
        p3.addCardToHand(new Card("F15", 15));
        p3.addCardToHand(new Card("F20", 20));
        p3.addCardToHand(new Card("F20", 20));
        p3.addCardToHand(new Card("F25", 25));
        p3.addCardToHand(new Card("F25", 25));
        p3.addCardToHand(new Card("F30", 30));
        p3.addCardToHand(new Card("F40", 40));
        p3.addCardToHand(new Card("L20",20));

        p4.addCardToHand(new Card("F5", 5));
        p4.addCardToHand(new Card("F5", 5));
        p4.addCardToHand(new Card("F10", 10));
        p4.addCardToHand(new Card("F15", 15));
        p4.addCardToHand(new Card("F15", 15));
        p4.addCardToHand(new Card("F20", 20));
        p4.addCardToHand(new Card("F20", 20));
        p4.addCardToHand(new Card("F25", 25));
        p4.addCardToHand(new Card("F25", 25));
        p4.addCardToHand(new Card("F30", 30));
        p4.addCardToHand(new Card("F50", 50));
        p4.addCardToHand(new Card("E30",30));

        play();
        return String.join("\n", messages);
    }

    public void play(){
        playTurn();
        resolveEvent(null);
    }

    // Set up adventure and event decks
    public void setUpDecks() {
        // Initialize the adventure deck with 100 cards
        adventureDeck = new Deck("Adventure", 100);
        adventureDeck.shuffle();
        // Initialize the event deck with 12 quest cards and 5 event cards
        eventDeck = new Deck("Event", 17);
        eventDeck.shuffle();
         List<Card> test = Arrays.asList(
                 new Card("Q3", 3 ),
//                 new Card("Plague", 0 ),
                 new Card("Queen’s Favor", 0 ),
                 new Card("Prosperity", 0 ));
        eventDeck.addOnTop(test);
    }

    // Distribute 12 cards
    public void distributeCards () {
        for (Player player : players) {
            for (int i = 0; i < 12; i++) {
                player.addCardToHand(adventureDeck.drawCard());
            }
        }
    }

    // Display current player’s turn, hand and the drawn event card
    public String playTurn() {
        try {
            Player currentPlayer = players.get(initialPlayerIndex);
            messages.add("Player " + (players.indexOf(currentPlayer) + 1) + "'s turn");


            currentPlayer.sortHand();
            messages.addAll(currentPlayer.displayHand());

            Card eventCard = eventDeck.drawCard();
            eventCard1 = new EventCard(eventCard.getType(), eventCard.getValue());
            messages.add("Event card drawn: " + eventCard);
            currentGameState = GameState.IDLE;

        } catch (Exception e) {
            messages.add("Error during playTurn: " + e.getMessage());
        }
        return String.join("\n", messages);
    }

    // Resolve the event or quest card drawn
    public String resolveEvent(String input) {
        if (currentGameState == GameState.IDLE) {
            if (eventCard1.isQuest()) {
                messages.add("---> A Quest is drawn <---");
                stageCount = eventCard1.getValue();
                currentGameState = GameState.PROMPTING_SPONSORSHIP;
                playerToAnswerIndex = initialPlayerIndex; // Start with the initial player
                // Starting with the current player
                if (input == null) {
                    messages.add("Player " + (playerToAnswerIndex + 1) + ", would you like to sponsor this quest? (yes/no)");
                    return String.join("\n", messages);
                }

                staticPlayersToTrim = trimHandForAll();
                trimmingIndex = 0;
                if (!staticPlayersToTrim.isEmpty()) {
                    System.out.println(staticPlayersToTrim);
                    currentGameState = GameState.TRIMMING_SPONSOR;
                    messages.add(prepareTrimmingMessage(staticPlayersToTrim.get(trimmingIndex)));
                    return String.join("\n", messages);
                }
                    endCurrentPlayerTurn();

            } else {
                System.out.println("---> A " + eventCard1.getType() + " event is drawn <---");

                if (input == null) {
                    messages.add(("---> A " + eventCard1.getType() + " event is drawn <---"));
                    String eventMessages = eventCard1.applyEvent(players.get(initialPlayerIndex), players, adventureDeck);
                    messages.add(eventMessages);
//                    if (players.get(initialPlayerIndex).getHandSize() > 12) {
//                        currentGameState = GameState.TRIMMING_EVENT;
//                        messages.add(("Hand needs to be trimmed. Press 'c' to continue trimming."));
//                        return String.join("\n", messages);
//                    }
                    return String.join("\n", messages);

                }

//                if (eventCard1.getType() == "Prosperity") {
//
//                }
//                staticPlayersToTrim = trimHandForAll();
//                trimmingIndex = 0;
//                if (!staticPlayersToTrim.isEmpty()) {
//                    System.out.println(staticPlayersToTrim);
//                    currentGameState = GameState.TRIMMING_HANDS;
//                    messages.add(prepareTrimmingMessage(staticPlayersToTrim.get(trimmingIndex)));
//                    return String.join("\n", messages);
//                }

                currentGameState = GameState.IDLE;
                endCurrentPlayerTurn();
                play();
                return String.join("\n", messages);
            }
            discardEPile.add(eventCard1);
        } else if (currentGameState == GameState.TRIMMING_SPONSOR) {
            return trimHandTo12SingleStep(input, sponsor);
        }
        else if (currentGameState == GameState.TRIMMING_EVENT) {
            return trimHandQueenFavor(input, players.get(initialPlayerIndex));

        } else if (currentGameState == GameState.TRIMMING_ONE_HAND) {
            return trimHandTo12(input, playerToTrim);
        } else if (currentGameState == GameState.PROMPTING_SPONSORSHIP) {
            return promptForSponsorship(input, stageCount);
        } else if (currentGameState == GameState.SETTING_UP_QUEST) {
            Player sponsor = players.get(sponsorIndex);
            System.out.println("TOTAL STAGES CARD____ " + input);
            String result = sponsor.setUpQuest(input, totalStagesCards, stageCount, currentStage);
            if (sponsor.hasFinishedQuestSetup()) {
                currentStage = 0;
                currentGameState = GameState.STARTING_QUEST;
                return resolveEvent(null);
            }
            else {
                currentStage = sponsor.getCurrentStage();
            }
//            return String.join("\n", messages);
            return result;
        }
        else if (currentGameState == GameState.STARTING_QUEST) {
            return startQuest(input, totalStagesCards);

        }
        else if (currentGameState == GameState.PROMPTING_PARTICIPATE) {
            return promptParticipantsForQuestStage(input, currentStage);
        } else if (currentGameState == GameState.BUILDING_ATTACK_CARD) {
            return buildAttackForParticipant(input);
        } else {
            messages.add("Error: Unexpected game state.");
            return String.join("\n", messages);
        }
        return String.join("\n", messages);
    }

    public List<Player> trimHandForAll() {
        List<Player> playersToTrim = new ArrayList<>();
        for (Player player : getPlayers()) {
            if (player.getHandSize() > 12) {
                playersToTrim.add(player);
            }
        }
        return playersToTrim;
    }

    private String prepareTrimmingMessage(Player player) {
        messages.clear();
        messages.add("!ATTENTION PLAYER " + (players.indexOf(player) + 1) + " : Your hand contains more than 12 cards. Please choose a card number (1 to " + player.getHandSize() + ") to discard:");
        player.sortHand();
        messages.addAll(player.displayHand());
        return String.join("\n", messages);
    }


    private String handleTrimmingPlayers(String input) {
        if (trimmingIndex < staticPlayersToTrim.size()) {
            Player currentPlayer = staticPlayersToTrim.get(trimmingIndex);

            if (input == null || input.trim().isEmpty()) {
                // Wait for the next input
                return prepareTrimmingMessage(currentPlayer);
            }

            // Process the input for the current player
            String trimMessage = trimHandTo12SingleStep(input, currentPlayer);
            messages.add("Your hand has been trimmed to 12 cards.");

            if (currentPlayer.getHandSize() <= 12) {
                trimmingIndex++; // Move to the next player
                if (trimmingIndex < staticPlayersToTrim.size()) {
                    Player nextPlayer = staticPlayersToTrim.get(trimmingIndex);
                    trimMessage += "\n" + prepareTrimmingMessage(nextPlayer);
                } else {
                    currentGameState = GameState.IDLE;
                    trimMessage += "\nAll players have trimmed their hands.";
                }
            }

            return trimMessage;
        } else {
            currentGameState = GameState.IDLE;
            return "All players have trimmed their hands.";
        }
    }


    public String trimHandTo12SingleStep(String input, Player player) {
        messages.clear();
        System.out.println("input: " + input);
        if (input == null || input.trim().isEmpty() || input.equalsIgnoreCase("c")) {
            return prepareTrimmingMessage(player);
        }

        try {
            int cardToDiscard = Integer.parseInt(input);

            if (cardToDiscard >= 1 && cardToDiscard <= player.getHandSize()) {
                Card removedCard = player.getHand().remove(cardToDiscard - 1);
                messages.add("You have discarded: " + removedCard);
                discardChosenCards(Collections.singletonList(removedCard));
            } else {
                messages.add("Invalid input. Please choose a valid card number (1 to " + player.getHandSize() + "):");
            }
        } catch (NumberFormatException e) {
            messages.add("Invalid input. Please enter a valid card number.");
        }

        if (player.getHandSize() > 12) {
            return prepareTrimmingMessage(player);
        }

        player.sortHand();
        messages.add("Finish trimming.");
        messages.add("Player " + (players.indexOf(sponsor) + 1) + "'s hand: " + sponsor.displayUpdatedHand());
        messages.add("Player " + (players.indexOf(sponsor) + 1) + "'s hand size: " + sponsor.getHandSize());
        endCurrentPlayerTurn();
        totalStagesCards.clear();
        System.out.println("TOTAL STAGES CARD__END QUEST__ " + totalStagesCards);
        return String.join("\n", messages);
    }

    public String trimHandQueenFavor(String input, Player player) {
        messages.clear();
        System.out.println("input: " + input);
        if (input == null || input.trim().isEmpty() || input.equalsIgnoreCase("c")) {
            return prepareTrimmingMessage(player);
        }

        try {
            int cardToDiscard = Integer.parseInt(input);

            if (cardToDiscard >= 1 && cardToDiscard <= player.getHandSize()) {
                Card removedCard = player.getHand().remove(cardToDiscard - 1);
                messages.add("You have discarded: " + removedCard);
              //  discardChosenCards(Collections.singletonList(removedCard));
            } else {
                messages.add("Invalid input. Please choose a valid card number (1 to " + player.getHandSize() + "):");
            }
        } catch (NumberFormatException e) {
            messages.add("Invalid input. Please enter a valid card number.");
        }

        if (player.getHandSize() > 12) {
            return prepareTrimmingMessage(player);
        }

        player.sortHand();
        messages.addAll(player.displayHand());
        messages.add("Finish trimming.");
//        endCurrentPlayerTurn();
        return String.join("\n", messages);
    }

    // Trim the player's hand to 12 cards
    public String trimHandTo12(String input, Player player) {
//        System.out.println("playerToTrim: " + (players.indexOf(playerToTrim) + 1));
        messages.clear();

        if (input == null || input.trim().isEmpty()) {
            return prepareTrimmingMessage(player);
        }

        try {
            int cardToDiscard = Integer.parseInt(input);
            if (cardToDiscard >= 1 && cardToDiscard <= player.getHandSize()) {
                Card removedCard = player.getHand().remove(cardToDiscard - 1);
                messages.add("You have discarded: " + removedCard);
                discardChosenCards(Collections.singletonList(removedCard));
            } else {
                messages.add("Invalid input. Please choose a valid card number (1 to " + player.getHandSize() + "):");
            }
            // return String.join("\n", messages);
        } catch (NumberFormatException e) {
            messages.add("Invalid input. Please enter a valid card number.");
        }

        player.sortHand();
        messages.addAll(player.displayHand());

//        System.out.println("curr Trim hand " + playerToTrim.getHandSize());
//        System.out.println("curr player:  " + (players.indexOf(eligibleParticipants.get(currentParticipantIndex)) + 1));
//        System.out.println("curr playerToTrim:  " + (players.indexOf(playerToTrim) + 1));
        if (playerToTrim.getHandSize() <=12) {
            messages.add("Your hand has been trimmed to 12 cards.");
//            System.out.println("currentParticipantIndex before " + currentParticipantIndex);
//            System.out.println("incr Trim " + currentParticipantIndex);
//            System.out.println("ell size " + (eligibleParticipants.size()-1));

            if (currentParticipantIndex == (eligibleParticipants.size()-1)) {
                messages.add("MOVE TO BUILD ATTACKS. Press 'c' to continue");
                currentGameState = GameState.BUILDING_ATTACK_CARD;
                messages.add("Participants remaining for the next stage:");
                for (Player eliPlayer : eligibleParticipants) {
                    messages.add("Player " + (players.indexOf(eliPlayer) + 1));
                }
                currentParticipantIndex = 0;
                return String.join("\n", messages);
            } else {

                currentGameState = GameState.PROMPTING_PARTICIPATE;
                ++currentParticipantIndex;
                messages.add("Player " + (players.indexOf(eligibleParticipants.get(currentParticipantIndex)) + 1) + ", would you like to continue or withdraw from the quest? (c/w)");
//                System.out.println("currentParticipantIndex after" + currentParticipantIndex);
            }
        }
        return String.join("\n", messages);
    }

    // A player chooses to sponsor the quest or not, starting from current player
    public String promptForSponsorship(String input, int stageCount) {
        messages.clear();
        Player currentPlayer = players.get(playerToAnswerIndex);
        if (currentGameState == GameState.PROMPTING_SPONSORSHIP) {
            if (input.equalsIgnoreCase("yes")) {
                messages.add("Player " + (playerToAnswerIndex + 1) + " has decided to sponsor the quest.");

                if (currentPlayer.canSponsorQuest(stageCount)) {
                    messages.add("Player " + (playerToAnswerIndex + 1) + " has enough cards to sponsor the quest.");
                    messages.add("---> A SPONSOR FOUND: " + "Player " + (playerToAnswerIndex + 1) + " type 'yes' to continue.");
                    sponsorIndex = playerToAnswerIndex;  // Set the sponsor index

                    currentGameState = GameState.SETTING_UP_QUEST;


                    return resolveEvent(null);
                } else {
                    System.out.println("playerToAnswer " + (playerToAnswerIndex + 1));
                    System.out.println("currentPlayer " + players.get(playerToAnswerIndex));
                    messages.add("Player " + (playerToAnswerIndex + 1) + " does not have enough cards to sponsor the quest.");
                }
                return String.join("\n", messages);
            } else if (input.equalsIgnoreCase("no")) {
                messages.add("Player " + (playerToAnswerIndex + 1) + " has decided to decline the quest.");
                if (playerToAnswerIndex == (players.size() -1)) {
                    messages.add("No players agreed to sponsor the quest.");
                    handleNoSponsorship(input);
                    return String.join("\n", messages);
                }
                messages.add("Player " + (playerToAnswerIndex + 2) + ", would you like to sponsor this quest? (yes/no)");
            } else {
                messages.add("Invalid input. Please input yes/no.");
                return String.join("\n", messages);
            }
        }

        // Move to the next player in turn
        playerToAnswerIndex = (playerToAnswerIndex + 1) % players.size();

//         Check if all players have been asked
        if (playerToAnswerIndex == initialPlayerIndex) {
            messages.add("No players agreed to sponsor the quest.");
            handleNoSponsorship(input);
        }
        return String.join("\n", messages);
    }

    public String handleNoSponsorship(String input) {
        messages.add("No one agreed to sponsor the quest. The quest has failed.");
        endCurrentPlayerTurn();
        return String.join("\n", messages);
    }

    // End the current player's turn and move to the next player
    public String endCurrentPlayerTurn() {
        messages.add("Player " + (initialPlayerIndex + 1) + "'s turn is over.");

        initialPlayerIndex = (initialPlayerIndex + 1) % players.size();
        currentGameState = GameState.IDLE;

//        displayHandsEndQuest(players);
        displayParticipantsShields(players);
        eligibleParticipants.clear();
        currentPlayerIndex = 0;
        checkForWinnersOrProceed();
        play();
        return String.join("\n", messages);
    }

    public String promptParticipantsForQuestStage(String input, int stageNumber) {
        messages.clear();
        // Check if there are no participants left
//        if (eligibleParticipants.isEmpty()) {
//            messages.add("MOVE TO BUILD ATTACKS //
//            currentGameState = GameState.;
//            return resolveEvent(null);
//        }

        // Get the current participant
        playerToTrim = eligibleParticipants.get(currentParticipantIndex);
        if (!updatedEligibleList.contains(playerToTrim)) {
            updatedEligibleList.add(playerToTrim);
        }

        // Validate input
        if (!input.equalsIgnoreCase("c") && !input.equalsIgnoreCase("w")) {
            messages.add("Invalid input. Please choose 'c' to continue or 'w' to withdraw.");
            return String.join("\n", messages);
        }

        // Process decision
        if (input.equalsIgnoreCase("w")) {
            messages.add("Player " + (players.indexOf(playerToTrim) + 1) + " has withdrawn from the quest.");
            withdrawnParticipants.add(playerToTrim);
            messages.add("Player " + (players.indexOf(playerToTrim) + 1) + "'s hand: " + playerToTrim.displayUpdatedHand());

            updatedEligibleList.remove(playerToTrim);
            messages.add("Player " + (players.indexOf(playerToTrim) + 2) + ", would you like to continue or withdraw from the quest? (c/w)");
        } else {
            messages.add("Player " + (players.indexOf(playerToTrim) + 1) + " is tackling the current stage.");
            Card drawnCard = adventureDeck.drawCard();
            playerToTrim.addCardToHand(drawnCard);
            messages.add("Player " + (players.indexOf(playerToTrim) + 1) + " drew: " + drawnCard);

            playerToTrim.sortHand();

            if (playerToTrim.getHandSize() > 12) {
                currentGameState = GameState.TRIMMING_ONE_HAND;
                prepareTrimmingMessage(playerToTrim);
                System.out.println(currentGameState);
                return String.join("\n", messages);
            }
        }

            if (currentParticipantIndex == (eligibleParticipants.size()-1)) {
                messages.add("MOVE TO BUILD ATTACKS FROM PROMPT. Press 'c' to continue");

                currentGameState = GameState.BUILDING_ATTACK_CARD;
                currentParticipantIndex = 0;
                eligibleParticipants.clear();
                eligibleParticipants.addAll(updatedEligibleList);
                return String.join("\n", messages);
            }
            currentGameState = GameState.PROMPTING_PARTICIPATE;
            ++currentParticipantIndex;
            messages.add("Player " + (players.indexOf(eligibleParticipants.get(currentParticipantIndex)) + 1) + ", would you like to continue or withdraw from the quest? (c/w)");

            return String.join("\n", messages);
    }

    private String buildAttackForParticipant(String input) {
        messages.clear();
        playerBuildingAttack = eligibleParticipants.get(currentPlayerIndex);

        messages.add("UPDATE_ELI: " + eligibleParticipants );

        List<Card> stageCards = totalStagesCards.get(currentStage - 1);
        weaponCards = filterWeaponCards(playerBuildingAttack.getHand());
        int stageValue = calculateStageValue(stageCards);
        if (weaponCards.isEmpty()) {
            messages.add("You have no weapon cards available. Attack value will be 0.");
            return String.join("\n", messages);
        }

//        System.out.println("eli par list: " + eligibleParticipants);

        if (input.equalsIgnoreCase("c")) {
            messages.add(" ");
            messages.add("-----> RESOLVING ATTACKS FOR STAGE " + (currentStage) + " (Stage value: " + stageValue + "):");
            messages.add("Player " + (players.indexOf(playerBuildingAttack) + 1) + ", please build your attack.");

            displayWeaponCards(weaponCards);
        return String.join("\n", messages);
        }

        try {
            int cardPosition = Integer.parseInt(input);

            if (cardPosition < 1 || cardPosition > weaponCards.size()) {
                messages.add("Invalid position. Please choose a valid card number (1 to " + weaponCards.size() + ").");
            } else {
                Card selectedCard = weaponCards.get(cardPosition - 1);

                // Check if the card was already chosen
                boolean alreadyChosen = false;
                for (Card chosenCard : chosenCards) {
                    if (chosenCard.getType().equals(selectedCard.getType())) {
                        alreadyChosen = true;
                        break;
                    }
                }

                if (alreadyChosen) {
                    messages.add("You have already selected this card. Please choose a different card.");
                } else {
                    playerBuildingAttack.addToAttack(selectedCard);
                    chosenCards.add(selectedCard);
                    // weaponCards.remove(cardPosition - 1);
                    messages.add("You have selected " + selectedCard + " for your attack.");
                }
                //displayWeaponCards(weaponCards);
            }
        } catch (NumberFormatException e) {
            // If the input is not a valid number, prompt the player again
//            messages.add("No attack value.");
        }

        if (input.equalsIgnoreCase("quit")) {
            messages.add("You have chosen to quit the attack phase.");

            if (!chosenCards.isEmpty()) {
                messages.add("Your final attack includes the following cards:");
                for (Card card : chosenCards) {
                    messages.add("- " + card);
                }
            }
            messages.add("Your final attack value is: " + playerBuildingAttack.getAttackValue());
            discardUsedAttackCards(playerBuildingAttack);

            resolveStage(playerBuildingAttack.getAttackValue(), stageValue, currentStage, stageCount);
            chosenCards.clear();

            System.out.println("stageValue: " + stageValue);
            System.out.println("currentStage: " + currentStage);
            System.out.println("stageCount: " + stageCount);
            System.out.println("currentPlayerIndex: " + currentPlayerIndex);
            System.out.println("current Player: " + (players.indexOf(eligibleParticipants.get(currentPlayerIndex)) + 1));

            if (currentPlayerIndex == (eligibleParticipants.size()-1)) {
                System.out.println("eli par list WHEN QUIT: " + eligibleParticipants);
                if (currentStage == stageCount) {
                    return endQuest(totalStagesCards, adventureDeck);
                }

                System.out.println("successfull else: " + successfulParticipants);

                currentPlayerIndex = 0;
                currentStage++;
//                eligibleParticipants = successfulParticipants;
//                messages.add("Player " + (players.indexOf(eligibleParticipants.get(currentPlayerIndex)) + 1) + ", would you like to continue or withdraw from the quest? (c/w)");
//                messages.add("Player " + (players.indexOf(eligibleParticipants.get(currentPlayerIndex)) + 1) + ", please build your attack.");
                weaponCards = filterWeaponCards(eligibleParticipants.get(currentPlayerIndex).getHand());
                displayWeaponCards(weaponCards);
                playerBuildingAttack.clearAttackCards();
                currentGameState = GameState.PROMPTING_PARTICIPATE;
                playerToTrim = playerBuildingAttack;

                // Update eligible participants for the next round
                if (withdrawnParticipants.size() == 4) {
                    messages.add("All participants have been eliminated.");
                    successfulParticipants.clear();
                    return endQuest(totalStagesCards, adventureDeck);
                } else {
                    eligibleParticipants.clear();
                    eligibleParticipants.addAll(successfulParticipants);
                    messages.add("Participants remaining for the next stage:");
                    for (Player player : eligibleParticipants) {
                        messages.add("Player " + (players.indexOf(player) + 1));
                    }
                    messages.add("Player " + (players.indexOf(eligibleParticipants.get(currentPlayerIndex)) + 1) + ", would you like to continue or withdraw from the quest? (c/w)");
                }

                return String.join("\n", messages);
            }



            playerBuildingAttack.clearAttackCards();
            currentPlayerIndex++;

            messages.add("Player " + (players.indexOf(eligibleParticipants.get(currentPlayerIndex)) + 1) + ", please build your attack.");
            weaponCards = filterWeaponCards(eligibleParticipants.get(currentPlayerIndex).getHand());
            displayWeaponCards(weaponCards);
        }
        return String.join("\n", messages);
    }

    public void resolveStage( int attackValue, int stageValue, int currentStage, int stageCount) {
        // Check if player succeeds or is eliminated
        if (attackValue < stageValue) {
            messages.add("Player " + (players.indexOf(playerBuildingAttack) + 1) + " is eliminated with an attack value of " + attackValue);
            messages.add("Player " + (players.indexOf(playerBuildingAttack) + 1) + " earns 0 shields.");
            messages.add("Player " + (players.indexOf(playerBuildingAttack) + 1) + "'s hand: " + playerBuildingAttack.displayUpdatedHand());
            messages.add("");
            withdrawnParticipants.add(playerBuildingAttack);
            playerBuildingAttack.displayUpdatedHand();
            successfulParticipants.remove(playerBuildingAttack);
            updatedEligibleList.remove(playerBuildingAttack);
        } else {
            messages.add("Player " + (players.indexOf(playerBuildingAttack) + 1) + " succeeds with an attack value of " + attackValue);
            messages.add("Player " + (players.indexOf(playerBuildingAttack) + 1) + " 's hand size: " + playerBuildingAttack.getHandSize());
            messages.add("");

            if (currentStage == stageCount) {
                playerBuildingAttack.addShields(stageCount);
                withdrawnParticipants.clear();
                successfulParticipants.clear();
                messages.add("Player " + (players.indexOf(playerBuildingAttack) + 1) + " wins the quest.");
                messages.add("Player " + (players.indexOf(playerBuildingAttack) + 1) + " earns " + stageCount + " shields.");
                messages.add("Player " + (players.indexOf(playerBuildingAttack) + 1) + "'s hand: " + playerBuildingAttack.displayUpdatedHand());
            } else {
                if (!successfulParticipants.contains(playerBuildingAttack)) {
                    successfulParticipants.add(playerBuildingAttack);
                }
            }
        }

//        System.out.println("Participants remaining for the next stage:: " + eligibleParticipants);
    }


    // Helper method to discard used attack cards after each stage
    private String discardUsedAttackCards(Player player) {
        List<Card> usedCards = player.getSelectedAttackCards();
        discardChosenCards(usedCards);
        player.getHand().removeAll(usedCards);

        messages.add("Player " + (players.indexOf(player) + 1) + " discarded the following attack cards:");
        for (Card card : usedCards) {
            messages.add("- " + card);
        }
        return String.join("\n", messages);
    }

    public String startQuest(String input, List<List<Card>> sponsorBuiltStages) {
        messages.clear();

        int totalStages = sponsorBuiltStages.size();
        List<Card> stageCards = sponsorBuiltStages.get(currentStage);

        Player sponsor = players.get(sponsorIndex);
        eligibleParticipants = new ArrayList<>(players);
        eligibleParticipants.remove(sponsor); // Sponsor doesn't participate
        withdrawnParticipants.add(sponsor);

        System.out.println("Current stage: " + currentStage);
        System.out.println("Current stage sponsor cards: " + stageCards);

        // Initialize eligible participants and current stage on the first call
        if (currentStage == 0) {
            if (eligibleParticipants.isEmpty()) {
                currentGameState = GameState.IDLE;
                return "No players are eligible to participate in this quest. The quest is canceled.";
            }

            currentStage = 1; // Start at stage 1
            messages.add("Quest begins! There are " + sponsorBuiltStages.size() + " stages.");
        }

        if (input == null) {
            currentGameState = GameState.PROMPTING_PARTICIPATE;
            displayEligibleParticipants(eligibleParticipants);
            playerToTrim = eligibleParticipants.get(currentParticipantIndex);
            messages.add("Player " + (players.indexOf(playerToTrim) + 1) + ", would you like to continue or withdraw from the quest? (c/w)");
            return String.join("\n", messages);
        }
        return String.join("\n", messages) + "\n" ;
    }

    // Calculate the total value of a stage based on its cards
    private int calculateStageValue(List<Card> stageCards) {
        return stageCards.stream().mapToInt(Card::getValue).sum(); // Sum up the card values
    }

    public void displayEligibleParticipants(List<Player> eligibleParticipants) {
        if (eligibleParticipants.isEmpty()) {
            messages.add("No players are eligible to participate in this quest.");
        } else {
            messages.add("Eligible participants for the quest:");
            for (Player player : eligibleParticipants) {
                int playerNumber = players.indexOf(player) + 1;
                messages.add("Player " + playerNumber);
            }
        }
    }

    public void displayParticipantsShields(List<Player> players) {
        messages.add("Players' total shields:");
        for (Player player : players) {
            int playerNumber = players.indexOf(player) + 1;
            messages.add("Player " + playerNumber + ": " + player.getTotalShield() + " shields");
        }

    }

    public void displayHandsEndQuest(List<Player> players) {
        messages.add("Players' total hand after quest:");
        for (Player player : players) {
            int playerNumber = players.indexOf(player) + 1;
            messages.add("Player " + (playerNumber + 1) + "'s hand: " + players.get(playerNumber).displayUpdatedHand());
        }
    }


    public String endQuest(List<List<Card>> stages, Deck adventureDeck) {
        messages.add("---> THE QUEST HAS ENDED <---");
        sponsor = players.get(sponsorIndex);

        // Sponsor discards all cards used to build the quest
        for (List<Card> stage : stages) {
            discardChosenCards(stage);
            sponsor.getHand().removeAll(stage);
        }

        messages.add("Sponsor discarded: " + stages);

        // Calculate the total number of cards discarded by the sponsor
        int totalDiscardedCards = stages.stream().mapToInt(List::size).sum();
        int cardsToDraw = totalDiscardedCards + stages.size();  // Cards to draw = discarded cards + number of stages

        messages.add("Sponsor will draw " + cardsToDraw + " new cards.");

        // Sponsor draws the cards
        for (int i = 0; i < cardsToDraw; i++) {
            Card drawnCard = adventureDeck.drawCard();
            if (drawnCard != null) {
                sponsor.addCardToHand(drawnCard);
                messages.add("Sponsor drew: " + drawnCard);
            } else {
                // If the deck is empty, reshuffle the discard pile and put it back into the adventure deck
                messages.add("No more cards left in the adventure deck to draw. Reshuffling the discard pile.");
                adventureDeck.reshuffle(discardAPile);
                i--;
            }
        }
        sponsor.sortHand();
        if (sponsor.getHandSize() > 12) {
            messages.add("Sponsor has more than 12 cards and needs to trim their hand. Press 'c' to continue.");
            currentGameState = GameState.TRIMMING_SPONSOR;
            withdrawnParticipants.clear();
            return String.join("\n", messages);
        }
        endCurrentPlayerTurn();
        withdrawnParticipants.clear();
//        eligibleParticipants.clear();
        totalStagesCards.clear();
        messages.add("T__END QUEST__  Participants: " + eligibleParticipants);
        displayHandsEndQuest(players);
        displayParticipantsShields(players);
        currentGameState = GameState.IDLE;

        return String.join("\n", messages);
    }

    // Filter weapon cards from the player's hand
    private List<Card> filterWeaponCards(List<Card> hand) {
        List<Card> weaponCards = new ArrayList<>();
        for (Card card : hand) {
            if (isWeaponCard(card)) {
                weaponCards.add(card);
            }
        }
        return weaponCards;
    }

    // Check if a card is a weapon card
    private boolean isWeaponCard(Card card) {
        return card.getType().startsWith("D") || card.getType().startsWith("H") ||
                card.getType().startsWith("S") || card.getType().startsWith("B") ||
                card.getType().startsWith("L") || card.getType().startsWith("E");
    }

    // Display available weapon cards
    private String displayWeaponCards(List<Card> weaponCards) {
        messages.add("Weapon cards in your hand:");
        for (int i = 0; i < weaponCards.size(); i++) {
            messages.add((i + 1) + ". " + weaponCards.get(i));
        }
        return String.join("\n", messages);
    }

    // Discard the selected cards after building the attack
    private void discardChosenCards(List<Card> chosenCards) {
        discardAPile.addAll(chosenCards);
    }

    public void checkForWinnersOrProceed() {
        for (Player player : players) {
            if (player.getTotalShield() >= 7) {
                winners.add(player);
            }
        }

        if (!winners.isEmpty()) {
            messages.add("---> We have one or more winners! <---");
            for (Player winner : winners) {
                messages.add("Player " + (players.indexOf(winner) + 1) + " has won the game with " + winner.getTotalShield() + " shields!");
            }

            messages.add("---> All players' total shields after Quest game: ");
            for (Player player : players) {
                messages.add("Player " + (players.indexOf(player) + 1) + ": " + player.getTotalShield() + " shields");
            }
        } else {
            messages.add("No players have won yet. Moving to the next player's turn.");

//            play();
        }
    }

    //Getters
    public int getAdventureDeckSize() { return adventureDeck.size(); }
    public int getEventDeckSize() { return eventDeck.size(); }
    public Deck getAdventureDeck() { return adventureDeck; }
    public Deck getEventDeck() { return eventDeck; }
    public List<Card> getDiscardAPile() { return discardAPile; }
    public int getTotalStagesCount() { return stageCount;}
    public List<List<Card>> getTotalStagesCards() { return totalStagesCards;}

    public List<Player> getWinners() {
        return winners;
    }
    public boolean isGameOver() {
        return !winners.isEmpty();
    }

    // Getter for players
    public List<Player> getPlayers() { return players; }
//    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
//    public Player getCurrentPlayer() { return players.get(currentPlayerIndex); }
    public String getMessages() { return String.join("\n", messages);}

    // Setters
//    public void setCurrentPlayerIndex(int index) {
//        if (index >= 0 && index < players.size()) {
//            this.currentPlayerIndex = index;
//        } else {
//            throw new IllegalArgumentException("Invalid player index: " + index);
//        }
//    }
//
//    public static void main(String[] args) {
//        Game game = new Game();
//        Scanner input = new Scanner(System.in);
//        PrintWriter output = new PrintWriter(System.out, true);
//
//        game.start();
//    }
}