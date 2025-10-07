package base;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the overall game state including hero, inventory, and current floor.
 */
public class GameState implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final Hero hero;
    private final Inventaire inventory;
    private int currentFloor;
    private String playerName;
    
    public GameState(Hero hero, String playerName) {
        this.hero = hero;
        this.inventory = new Inventaire(hero);
        this.currentFloor = 0;
        this.playerName = playerName;
    }
    
    // Getters
    public Hero getHero() {
        return hero;
    }
    
    public Inventaire getInventory() {
        return inventory;
    }
    
    public int getCurrentFloor() {
        return currentFloor;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    // Setters
    public void setCurrentFloor(int floor) {
        this.currentFloor = floor;
        this.hero.setFloor(floor);
    }
    
    public void advanceToNextFloor() {
        this.currentFloor++;
        this.hero.setFloor(this.currentFloor);
    }
    
    /**
     * Save game state to file.
     */
    public void save(String filename) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(this);
            System.out.println("Game saved successfully to " + filename);
        }
    }
    
    /**
     * Load game state from file.
     */
    public static GameState load(String filename) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            GameState state = (GameState) in.readObject();
            System.out.println("Game loaded successfully from " + filename);
            return state;
        }
    }
    
    /**
     * Create a new game with the specified hero type and player name.
     */
    public static GameState newGame(Hero.HeroType heroType, String playerName) {
        Hero hero = new Hero(heroType);
        return new GameState(hero, playerName);
    }
    
    @Override
    public String toString() {
        return String.format("GameState[Player: %s, Floor: %d, %s]",
                playerName, currentFloor, hero);
    }
}
