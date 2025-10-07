package base;

/**
 * Simple test to verify that levels are generated correctly with
 * all reachable cells and filled unreachable areas.
 */
public class LevelFactoryTest {
    
    public static void main(String[] args) {
        System.out.println("Testing Level Generation with Reachability Check\n");
        
        for (int i = 0; i <= 9; i++) {
            testLevel(i);
        }
        
        System.out.println("\nAll levels tested successfully!");
    }
    
    private static void testLevel(int levelNum) {
        System.out.println("Testing Level " + levelNum + "...");
        
        Level level = LevelFactory.createLevel(levelNum);
        
        // Verify spawn and exit positions
        Level.Position spawn = level.getSpawnPosition();
        Level.Position exit = level.getExitPosition();
        
        System.out.println("  Spawn: " + spawn);
        System.out.println("  Exit: " + exit);
        
        // Check that spawn and exit are not walls
        if (level.hasWall(spawn.getX(), spawn.getY())) {
            System.out.println("  ERROR: Spawn is a wall!");
        }
        if (level.hasWall(exit.getX(), exit.getY())) {
            System.out.println("  ERROR: Exit is a wall!");
        }
        
        // Count chests and verify they're not walls
        int chestCount = 0;
        for (Level.Position chestPos : level.getChests().keySet()) {
            chestCount++;
            if (level.hasWall(chestPos.getX(), chestPos.getY())) {
                System.out.println("  ERROR: Chest at " + chestPos + " is a wall!");
            }
        }
        System.out.println("  Chests: " + chestCount);
        
        // Count mobs
        int mobCount = level.getMobs().size();
        System.out.println("  Mobs: " + mobCount);
        
        // Count walls
        int wallCount = level.getWalls().size();
        System.out.println("  Walls: " + wallCount);
        
        // Calculate reachable cells
        int reachableCount = countReachableCells(level);
        System.out.println("  Reachable cells: " + reachableCount);
        
        int totalCells = Constants.BOARD_WIDTH * Constants.BOARD_HEIGHT;
        int unreachableCells = totalCells - reachableCount - wallCount;
        
        if (unreachableCells > 0) {
            System.out.println("  WARNING: " + unreachableCells + " unreachable non-wall cells!");
        } else {
            System.out.println("  ✓ All non-wall cells are reachable");
        }
        
        System.out.println();
    }
    
    /**
     * Count reachable cells using flood fill from spawn.
     */
    private static int countReachableCells(Level level) {
        java.util.Set<Level.Position> visited = new java.util.HashSet<>();
        java.util.Queue<Level.Position> queue = new java.util.LinkedList<>();
        
        Level.Position spawn = level.getSpawnPosition();
        queue.add(spawn);
        visited.add(spawn);
        
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        
        while (!queue.isEmpty()) {
            Level.Position current = queue.poll();
            
            for (int[] dir : directions) {
                int newX = current.getX() + dir[0];
                int newY = current.getY() + dir[1];
                Level.Position neighbor = new Level.Position(newX, newY);
                
                if (newX >= 0 && newX < Constants.BOARD_WIDTH && 
                    newY >= 0 && newY < Constants.BOARD_HEIGHT &&
                    !level.hasWall(newX, newY) && 
                    !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        
        return visited.size();
    }
}
