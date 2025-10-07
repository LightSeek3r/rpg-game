package base;

import java.io.IOException;

/**
 * Utility class for saving game state.
 * @deprecated Use GameState.save() directly instead.
 */
@Deprecated
public class Sauvegarde {
    
    private static final String DEFAULT_SAVE_FILE = "Sauvegarde.sav";
    
    /**
     * Save the current game state.
     */
    public static void save(GameState gameState) throws IOException {
        save(gameState, DEFAULT_SAVE_FILE);
    }
    
    /**
     * Save the game state to a specific file.
     */
    public static void save(GameState gameState, String filename) throws IOException {
        gameState.save(filename);
        System.out.println("Game saved successfully!");
    }
    
    /**
     * Quick save method for legacy compatibility.
     */
    public static boolean quickSave(GameState gameState) {
        try {
            save(gameState);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
