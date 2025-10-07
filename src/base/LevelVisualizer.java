package base;

/**
 * Visualize a level in ASCII format to see the reachability.
 */
public class LevelVisualizer {
    
    public static void main(String[] args) {
        // Visualize level 0 as an example
        System.out.println("Level 0 Visualization\n");
        visualizeLevel(0);
    }
    
    public static void visualizeLevel(int levelNum) {
        Level level = LevelFactory.createLevel(levelNum);
        
        System.out.println("Legend:");
        System.out.println("  # = Wall");
        System.out.println("  S = Spawn");
        System.out.println("  E = Exit");
        System.out.println("  C = Chest");
        System.out.println("  M = Mob");
        System.out.println("  . = Empty floor");
        System.out.println();
        
        // Create a character grid
        char[][] grid = new char[Constants.BOARD_HEIGHT][Constants.BOARD_WIDTH];
        
        // Initialize with empty spaces
        for (int y = 0; y < Constants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
                if (level.hasWall(x, y)) {
                    grid[y][x] = '#';
                } else {
                    grid[y][x] = '.';
                }
            }
        }
        
        // Mark chests
        for (Level.Position pos : level.getChests().keySet()) {
            grid[pos.getY()][pos.getX()] = 'C';
        }
        
        // Mark mobs
        for (Level.Position pos : level.getMobs().keySet()) {
            grid[pos.getY()][pos.getX()] = 'M';
        }
        
        // Mark spawn and exit (these override other markers)
        Level.Position spawn = level.getSpawnPosition();
        Level.Position exit = level.getExitPosition();
        grid[spawn.getY()][spawn.getX()] = 'S';
        grid[exit.getY()][exit.getX()] = 'E';
        
        // Print the grid
        for (int y = 0; y < Constants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.println();
        }
        
        System.out.println();
        
        // Print statistics
        System.out.println("Statistics:");
        System.out.println("  Walls: " + level.getWalls().size());
        System.out.println("  Chests: " + level.getChests().size());
        System.out.println("  Mobs: " + level.getMobs().size());
        
        // Verify all important positions are reachable
        java.util.Set<Level.Position> reachable = getReachableCells(level);
        System.out.println("  Reachable cells: " + reachable.size());
        
        boolean exitReachable = reachable.contains(exit);
        System.out.println("  Exit reachable: " + (exitReachable ? "YES" : "NO"));
        
        int unreachableChests = 0;
        for (Level.Position pos : level.getChests().keySet()) {
            if (!reachable.contains(pos)) {
                unreachableChests++;
            }
        }
        
        if (unreachableChests > 0) {
            System.out.println("  WARNING: " + unreachableChests + " chest(s) are unreachable!");
        } else {
            System.out.println("  All chests are reachable: YES");
        }
    }
    
    private static java.util.Set<Level.Position> getReachableCells(Level level) {
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
        
        return visited;
    }
}
