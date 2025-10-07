package base;

import java.io.IOException;

/**
 * Utility class for loading game state.
 */
@Deprecated
public class Importer {
    
    private static final String DEFAULT_SAVE_FILE = "Sauvegarde.sav";
    
    /**
     * Load the game state from the default save file.
     */
    public static GameState load() throws IOException, ClassNotFoundException {
        return load(DEFAULT_SAVE_FILE);
    }
    
    /**
     * Load the game state from a specific file.
     */
    public static GameState load(String filename) throws IOException, ClassNotFoundException {
        GameState state = GameState.load(filename);
        System.out.println("Game loaded successfully!");
        return state;
    }
    
    /**
     * Quick load method for legacy compatibility.
     */
    public static GameState quickLoad() {
        try {
            return load();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Check if a save file exists.
     */
    public static boolean saveFileExists() {
        return saveFileExists(DEFAULT_SAVE_FILE);
    }
    
    /**
     * Check if a specific save file exists.
     */
    public static boolean saveFileExists(String filename) {
        return new java.io.File(filename).exists();
    }
}