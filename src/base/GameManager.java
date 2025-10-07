package base;

import java.util.Map;

/**
 * Main game manager that coordinates all game systems.
 * Replaces the scattered logic in RpgGraphe.java
 */
public class GameManager {
    
    private final GameState gameState;
    private Level currentLevel;
    private Level.Position playerPosition;
    private CombatManager combatManager;
    private ChestOpenCallback chestOpenCallback;
    private LevelUpCallback levelUpCallback;
    
    public GameManager(GameState gameState) {
        this.gameState = gameState;
        this.combatManager = new CombatManager(gameState.getHero());
        loadLevel(gameState.getCurrentFloor());
    }
    
    /**
     * Set callback for chest opening events.
     */
    public void setChestOpenCallback(ChestOpenCallback callback) {
        this.chestOpenCallback = callback;
    }
    
    /**
     * Set callback for level up events.
     */
    public void setLevelUpCallback(LevelUpCallback callback) {
        this.levelUpCallback = callback;
    }
    
    /**
     * Load a specific floor/level.
     */
    public void loadLevel(int floorNumber) {
        this.currentLevel = LevelFactory.createLevel(floorNumber);
        this.playerPosition = currentLevel.getSpawnPosition();
        gameState.setCurrentFloor(floorNumber);
    }
    
    /**
     * Move player to next floor.
     */
    public void advanceToNextFloor() {
        int nextFloor = gameState.getCurrentFloor() + 1;
        if (nextFloor >= Constants.MAX_FLOORS) {
            // Game complete!
            System.out.println("Félicitations ! Vous avez terminé le jeu !");
            return;
        }
        
        loadLevel(nextFloor);
        System.out.println("Avancé au niveau " + nextFloor);
    }
    
    /**
     * Attempt to move the player.
     * @return true if move was successful, false otherwise
     */
    public MoveResult movePlayer(Direction direction) {
        int newX = playerPosition.getX();
        int newY = playerPosition.getY();
        
        // Update hero's facing direction based on movement
        switch (direction) {
            case UP:
                newY--;
                gameState.getHero().setFacingDirection(Hero.Direction.BACK);
                break;
            case DOWN:
                newY++;
                gameState.getHero().setFacingDirection(Hero.Direction.FRONT);
                break;
            case LEFT:
                newX--;
                gameState.getHero().setFacingDirection(Hero.Direction.LEFT);
                break;
            case RIGHT:
                newX++;
                gameState.getHero().setFacingDirection(Hero.Direction.RIGHT);
                break;
        }
        
        // Check bounds first
        if (newX < 0 || newX >= Constants.BOARD_WIDTH || newY < 0 || newY >= Constants.BOARD_HEIGHT) {
            return new MoveResult(false, "Impossible de se déplacer là - hors limites !");
        }
        
        // Check for wall
        if (currentLevel.hasWall(newX, newY)) {
            return new MoveResult(false, "Impossible de se déplacer là - bloqué par un mur !");
        }
        
        // Check for mob - start combat but don't move
        if (currentLevel.hasMob(newX, newY)) {
            Mob mob = currentLevel.getMob(newX, newY);
            combatManager.startCombat(mob, newX, newY);
            return new MoveResult(false, "Combat commencé avec " + mob.getType() + "!");
        }

        // Check for nearby boss (within 1 tile) BEFORE allowing movement
        Level.Position nearbyBossPos = findNearbyBoss(newX, newY, 1);
        if (nearbyBossPos != null) {
            Mob boss = currentLevel.getMob(nearbyBossPos.getX(), nearbyBossPos.getY());
            combatManager.startCombat(boss, nearbyBossPos.getX(), nearbyBossPos.getY());
            return new MoveResult(false, "Le boss vous a repéré ! Combat commencé !");
        }
        
        // Check for chest
        if (currentLevel.hasChest(newX, newY)) {
            Level.Chest chest = currentLevel.getChest(newX, newY);
            if (!chest.isOpened()) {
                openChest(newX, newY);
                return new MoveResult(true, "Trouvé un coffre !");
            }
        }
        
        // Check for exit
        if (currentLevel.isExit(newX, newY)) {
            // Check if boss is still alive
            if (currentLevel.hasBossAlive()) {
                return new MoveResult(false, "Le boss doit être vaincu avant de quitter ce palier !");
            }
            advanceToNextFloor();
            return new MoveResult(true, "Déplacement vers le prochain étage...");
        }
        
        // Move successful
        playerPosition = new Level.Position(newX, newY);
        return new MoveResult(true, "Déplacé " + direction.toFrench());
    }
    
    /**
     * Find a nearby boss within the specified distance.
     * @param x Player's X position
     * @param y Player's Y position
     * @param maxDistance Maximum distance to check (in tiles)
     * @return Position of nearby boss, or null if none found
     */
    private Level.Position findNearbyBoss(int x, int y, int maxDistance) {
        for (Map.Entry<Level.Position, Mob> entry : currentLevel.getMobs().entrySet()) {
            Mob mob = entry.getValue();
            if (mob.getType() == Mob.MobType.BOSS) {
                Level.Position bossPos = entry.getKey();
                int distance = Math.abs(bossPos.getX() - x) + Math.abs(bossPos.getY() - y);
                if (distance <= maxDistance) {
                    return bossPos;
                }
            }
        }
        return null;
    }
    
    /**
     * Open a chest at the given position.
     */
    private void openChest(int x, int y) {
        Level.Chest chest = currentLevel.getChest(x, y);
        if (chest == null || chest.isOpened()) {
            return;
        }
        
        chest.open();
        Inventaire inventory = gameState.getInventory();
        boolean inventoryFull = false;
        
        for (Equipment item : chest.getItems()) {
            if (inventory.addItem(item)) {
                // Item added successfully
            } else {
                inventoryFull = true;
            }
        }
        
        // Notify via callback if set
        if (chestOpenCallback != null) {
            chestOpenCallback.onChestOpen(chest, inventoryFull);
        }
    }
    
    /**
     * Player attacks in combat.
     */
    public CombatManager.CombatResult playerAttack() {
        if (!combatManager.isCombatActive()) {
            return new CombatManager.CombatResult(false, "Pas de combat actif");
        }
        
        CombatManager.CombatResult result = combatManager.heroAttack();
        
        // If enemy defeated, remove from level
        if (result.isSuccess() && !combatManager.isCombatActive()) {
            // Combat ended due to mob defeat - remove it from the level
            int mobX = combatManager.getEnemyX();
            int mobY = combatManager.getEnemyY();
            currentLevel.removeMob(mobX, mobY);
        }
        
        return result;
    }
    
    /**
     * Enemy turn in combat.
     */
    public CombatManager.CombatResult enemyTurn() {
        return combatManager.enemyTurn();
    }
    
    /**
     * Use a potion from inventory.
     */
    public boolean usePotion(Equipment potion) {
        if (potion.getType() != Equipment.EquipmentType.POTION) {
            return false;
        }
        
        return gameState.getInventory().useItem(potion);
    }
    
    /**
     * Save the game.
     */
    public boolean saveGame(String filename) {
        try {
            gameState.save(filename);
            System.out.println("Game saved successfully!");
            return true;
        } catch (Exception e) {
            System.err.println("Failed to save game: " + e.getMessage());
            e.printStackTrace();  // Print full stack trace for debugging
            return false;
        }
    }
    
    /**
     * Load a saved game.
     */
    public static GameManager loadGame(String filename) {
        try {
            GameState state = GameState.load(filename);
            return new GameManager(state);
        } catch (Exception e) {
            System.err.println("Failed to load game: " + e.getMessage());
            return null;
        }
    }
    
    // Getters
    public GameState getGameState() { return gameState; }
    public Level getCurrentLevel() { return currentLevel; }
    public Level.Position getPlayerPosition() { return playerPosition; }
    public CombatManager getCombatManager() { return combatManager; }
    public Hero getHero() { return gameState.getHero(); }
    public Inventaire getInventory() { return gameState.getInventory(); }
    
    /**
     * Check if game is over (hero dead).
     */
    public boolean isGameOver() {
        return !gameState.getHero().isAlive();
    }
    
    /**
     * Check if game is won (completed all floors).
     */
    public boolean isGameWon() {
        return gameState.getCurrentFloor() >= Constants.MAX_FLOORS;
    }
    
    /**
     * Callback interface for chest opening events.
     */
    public interface ChestOpenCallback {
        void onChestOpen(Level.Chest chest, boolean inventoryFull);
    }
    
    /**
     * Callback interface for level up events.
     */
    public interface LevelUpCallback {
        void onLevelUp(Hero hero);
    }
    
    /**
     * Direction enum for movement.
     */
    public enum Direction {
        UP, DOWN, LEFT, RIGHT;
        
        /**
         * Get the French translation of the direction.
         */
        public String toFrench() {
            switch (this) {
                case UP: return "en haut";
                case DOWN: return "en bas";
                case LEFT: return "à gauche";
                case RIGHT: return "à droite";
                default: return this.name().toLowerCase();
            }
        }
    }
    
    /**
     * Result of a move attempt.
     */
    public static class MoveResult {
        private final boolean success;
        private final String message;
        
        public MoveResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
}
