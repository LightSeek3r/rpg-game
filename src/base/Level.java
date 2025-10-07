package base;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a game level/floor with walls, chests, mobs, and exit.
 */
public class Level implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final int floorNumber;
    private final Set<Position> walls;
    private final Map<Position, Chest> chests;
    private final Map<Position, Mob> mobs;
    private final Position exitPosition;
    private final Position spawnPosition;
    
    public Level(int floorNumber, Set<Position> walls, Map<Position, Chest> chests,
                 Map<Position, Mob> mobs, Position exitPosition, Position spawnPosition) {
        this.floorNumber = floorNumber;
        this.walls = walls;
        this.chests = chests;
        this.mobs = mobs;
        this.exitPosition = exitPosition;
        this.spawnPosition = spawnPosition;
    }
    
    /**
     * Check if a position has a wall.
     */
    public boolean hasWall(int x, int y) {
        return walls.contains(new Position(x, y));
    }
    
    /**
     * Check if a position has a chest.
     */
    public boolean hasChest(int x, int y) {
        return chests.containsKey(new Position(x, y));
    }
    
    /**
     * Get chest at position.
     */
    public Chest getChest(int x, int y) {
        return chests.get(new Position(x, y));
    }
    
    /**
     * Remove chest at position (after opening).
     */
    public void removeChest(int x, int y) {
        chests.remove(new Position(x, y));
    }
    
    /**
     * Check if a position has a mob.
     */
    public boolean hasMob(int x, int y) {
        return mobs.containsKey(new Position(x, y));
    }
    
    /**
     * Get mob at position.
     */
    public Mob getMob(int x, int y) {
        return mobs.get(new Position(x, y));
    }
    
    /**
     * Remove mob at position (after defeat).
     */
    public void removeMob(int x, int y) {
        mobs.remove(new Position(x, y));
    }
    
    /**
     * Check if position is the exit.
     */
    public boolean isExit(int x, int y) {
        return exitPosition.equals(new Position(x, y));
    }
    
    /**
     * Check if there is a boss alive on this level.
     */
    public boolean hasBossAlive() {
        for (Mob mob : mobs.values()) {
            if (mob.getType() == Mob.MobType.BOSS) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if movement to position is valid.
     */
    public boolean isValidMove(int x, int y) {
        // Check bounds
        if (x < 0 || x >= Constants.BOARD_WIDTH || y < 0 || y >= Constants.BOARD_HEIGHT) {
            return false;
        }
        
        // Check for wall
        if (hasWall(x, y)) {
            return false;
        }
        
        // Check for mob - players cannot pass through mobs
        if (hasMob(x, y)) {
            return false;
        }
        
        return true;
    }
    
    // Getters
    public int getFloorNumber() { return floorNumber; }
    public Position getSpawnPosition() { return spawnPosition; }
    public Position getExitPosition() { return exitPosition; }
    public Set<Position> getWalls() { return new HashSet<>(walls); }
    public Map<Position, Chest> getChests() { return new HashMap<>(chests); }
    public Map<Position, Mob> getMobs() { return new HashMap<>(mobs); }
    
    /**
     * Position on the game board.
     */
    public static class Position implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private final int x;
        private final int y;
        
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public int getX() { return x; }
        public int getY() { return y; }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
        
        @Override
        public String toString() {
            return String.format("(%d, %d)", x, y);
        }
    }
    
    /**
     * Chest containing loot.
     */
    public static class Chest implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private final int id;
        private final List<Equipment> items;
        private boolean opened;
        
        public Chest(int id, List<Equipment> items) {
            this.id = id;
            this.items = items;
            this.opened = false;
        }
        
        public int getId() { return id; }
        public List<Equipment> getItems() { return new ArrayList<>(items); }
        public boolean isOpened() { return opened; }
        
        public void open() {
            this.opened = true;
        }
    }
}
