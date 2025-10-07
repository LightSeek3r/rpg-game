package base;

/**
 * Game constants and configuration values.
 */
public final class Constants {
    
    // Prevent instantiation
    private Constants() {}
    
    // Board dimensions
    public static final int BOARD_WIDTH = 30;
    public static final int BOARD_HEIGHT = 30;
    
    // Inventory
    public static final int MAX_INVENTORY_SIZE = 84;
    public static final int INVENTORY_GRID_COLS = 14;
    public static final int INVENTORY_GRID_ROWS = 14;
    
    // Combat
    public static final int BASE_ATTACK_PA_COST = 3;
    public static final int SPECIAL_ATTACK_PA_COST = 2;
    public static final int BOSS_SPECIAL_PA_COST = 1;
    
    // Experience thresholds for levels
    public static final int[] XP_THRESHOLDS = {
        0,      // Level 1
        500,    // Level 2
        700,    // Level 3
        800,    // Level 4
        1000,   // Level 5
        1400,   // Level 6
        1800,   // Level 7
        2400,   // Level 8
        2800,   // Level 9
        3000,   // Level 10
        3800,   // Level 11
        4600,   // Level 12
        5400,   // Level 13
        6200,   // Level 14
        7000,   // Level 15
        8600,   // Level 16
        10200,  // Level 17
        11800,  // Level 18
        13400,  // Level 19
        15000,  // Level 20
        17000,  // Level 21
        19000,  // Level 22
        21000,  // Level 23
        23000,  // Level 24
        25000,  // Level 25
        28000,  // Level 26
        30000,  // Level 27
        35000,  // Level 28
        40000,  // Level 29
        50000   // Level 30
    };
    
    // Level up bonuses
    public static final int HP_BONUS_EARLY = 5;       // Levels 1-4
    public static final int HP_BONUS_MID_LOW = 10;    // Levels 5-10
    public static final int HP_BONUS_MID_HIGH = 15;   // Levels 11-15
    public static final int HP_BONUS_LATE_LOW = 20;   // Levels 16-20
    public static final int HP_BONUS_LATE_HIGH = 25;  // Levels 21-25
    public static final int HP_BONUS_END = 30;        // Levels 26-29
    public static final int HP_BONUS_SPECIAL_10 = 100;
    public static final int HP_BONUS_SPECIAL_20 = 100;
    public static final int HP_BONUS_MAX_LEVEL = 500;
    
    public static final int STAT_POINTS_PER_LEVEL = 5;
    public static final int PRIMARY_STAT_BONUS = 10;
    
    // File paths
    public static final String IMAGE_DIR = "./images/";
    public static final String DEFAULT_SAVE_FILE = "Sauvegarde.sav";
    
    // Max values
    public static final int MAX_LEVEL = 30;
    public static final int MAX_FLOORS = 10;
}
