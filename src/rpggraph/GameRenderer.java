package rpggraph;

import base.*;
import ihm.FrontEnd;

/**
 * Handles rendering the game board to the FrontEnd display
 */
public class GameRenderer {
    
    private final FrontEnd frontEnd;
    private final String imageDir;
    
    public GameRenderer(FrontEnd frontEnd) {
        this.frontEnd = frontEnd;
        this.imageDir = Constants.IMAGE_DIR;
    }
    
    /**
     * Render the entire game state.
     */
    public void render(GameManager gameManager) {
        Level level = gameManager.getCurrentLevel();
        Level.Position playerPos = gameManager.getPlayerPosition();
        Hero hero = gameManager.getHero();
        
        // Render floor tiles first (no repaint yet)
        renderFloor(false);
        
        // Render walls (no repaint yet)
        renderWalls(level, false);
        
        // Render chests (no repaint yet)
        renderChests(level, false);
        
        // Render mobs (no repaint yet)
        renderMobs(level, false);
        
        // Render exit (no repaint yet)
        renderExit(level, false);
        
        // Render player (on top, still no repaint)
        renderPlayer(playerPos, hero, false);
        
        // Force single update of the display at the end
        frontEnd.updateDisplay();
    }
    
    /**
     * Clear the board.
     */
    @SuppressWarnings("unused")
    private void clearBoard() {
        for (int y = 0; y < Constants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
                frontEnd.setBackground(x, y, imageDir + "Sol.png");
            }
        }
    }
    
    /**
     * Render floor tiles.
     */
    private void renderFloor(boolean repaint) {
        for (int y = 1; y < Constants.BOARD_HEIGHT - 1; y++) {
            for (int x = 1; x < Constants.BOARD_WIDTH - 1; x++) {
                frontEnd.setBackground(x, y, imageDir + "Sol.png");
            }
        }
    }
    
    /**
     * Render walls.
     */
    private void renderWalls(Level level, boolean repaint) {
        for (Level.Position pos : level.getWalls()) {
            frontEnd.setBackground(pos.getX(), pos.getY(), imageDir + "Mur.png");
        }
    }
    
    /**
     * Render chests.
     */
    private void renderChests(Level level, boolean repaint) {
        for (Level.Position pos : level.getChests().keySet()) {
            Level.Chest chest = level.getChest(pos.getX(), pos.getY());
            String image = chest.isOpened() ? "coffre-ouvert.png" : "Coffre.png";
            frontEnd.setBackground(pos.getX(), pos.getY(), imageDir + image);
        }
    }
    
    /**
     * Render mobs.
     */
    private void renderMobs(Level level, boolean repaint) {
        for (Level.Position pos : level.getMobs().keySet()) {
            Mob mob = level.getMob(pos.getX(), pos.getY());
            if (mob != null && mob.isAlive()) {
                String image = getMobImage(mob);
                frontEnd.setBackground(pos.getX(), pos.getY(), imageDir + image);
            }
        }
    }
    
    /**
     * Render the exit portal.
     */
    private void renderExit(Level level, boolean repaint) {
        Level.Position exit = level.getExitPosition();
        frontEnd.setBackground(exit.getX(), exit.getY(), imageDir + "BoulePortail.png");
    }
    
    /**
     * Render the player character.
     */
    private void renderPlayer(Level.Position pos, Hero hero, boolean repaint) {
        // Convert Hero.Direction to GameRenderer.Direction
        Direction direction = convertHeroDirection(hero.getFacingDirection());
        String image = getHeroImage(hero, direction);
        frontEnd.setBackground(pos.getX(), pos.getY(), imageDir + image);
    }
    
    /**
     * Convert Hero.Direction to GameRenderer.Direction.
     */
    private Direction convertHeroDirection(Hero.Direction heroDirection) {
        switch (heroDirection) {
            case FRONT:
                return Direction.FRONT;
            case BACK:
                return Direction.BACK;
            case LEFT:
                return Direction.LEFT;
            case RIGHT:
                return Direction.RIGHT;
            default:
                return Direction.FRONT;
        }
    }
    
    /**
     * Get the appropriate mob image.
     */
    private String getMobImage(Mob mob) {
        switch (mob.getType()) {
            case SMALL:
                return "Mob1F.png";
            case LARGE:
                return "Mob2F.png";
            case BOSS:
                return "Mob4F.png";
            default:
                return "Mob1F.png";
        }
    }
    
    /**
     * Get the appropriate hero image based on type and direction.
     */
    private String getHeroImage(Hero hero, Direction direction) {
        String heroType;
        switch (hero.getType()) {
            case WARRIOR:
                heroType = "Guerrier";
                break;
            case ARCHER:
                heroType = "Archer";
                break;
            case MAGE:
                heroType = "Mage";
                break;
            default:
                heroType = "Guerrier";
        }
        
        String gender = "M"; // Male by default, could be a hero property
        String dir;
        switch (direction) {
            case FRONT:
                dir = "F";
                break;
            case BACK:
                dir = "B";
                break;
            case LEFT:
                dir = "L";
                break;
            case RIGHT:
                dir = "R";
                break;
            default:
                dir = "F";
        }
        
        return heroType + gender + dir + ".png";
    }
    
    /**
     * Direction enum for hero sprite.
     */
    public enum Direction {
        FRONT, BACK, LEFT, RIGHT
    }
}
