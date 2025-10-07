package base;

import java.io.Serializable;

/**
 * Represents a hero character in the game.
 */
public class Hero implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public enum HeroType {
        ARCHER(50, 10, 30, 20),
        WARRIOR(50, 30, 20, 10),
        MAGE(50, 20, 10, 30);
        
        private final int basePV;
        private final int baseForce;
        private final int baseAgility;
        private final int baseWisdom;
        
        HeroType(int pv, int force, int agility, int wisdom) {
            this.basePV = pv;
            this.baseForce = force;
            this.baseAgility = agility;
            this.baseWisdom = wisdom;
        }
    }
    
    private final HeroType type;
    private int pv;              // Health Points
    private int maxPV;           // Maximum Health Points
    private int xp;              // Experience
    private int force;
    private int agility;
    private int wisdom;
    private int level;
    private int floor;
    private int unspentStatPoints;
    private Direction facingDirection;  // Current direction the hero is facing
    
    // Combat stats
    private int damage;
    private int resistance;
    private transient LevelUpCallback levelUpCallback;  // transient = not serialized
    
    /**
     * Direction enum for hero facing.
     */
    public enum Direction {
        FRONT, BACK, LEFT, RIGHT
    }
    
    public Hero(HeroType type) {
        this.type = type;
        this.pv = type.basePV;
        this.maxPV = type.basePV;
        this.force = type.baseForce;
        this.agility = type.baseAgility;
        this.wisdom = type.baseWisdom;
        this.level = 1;
        this.xp = 0;
        this.floor = 0;
        this.unspentStatPoints = 0;
        this.damage = 0;
        this.resistance = 0;
        this.facingDirection = Direction.FRONT;  // Default facing direction
    }
    
    /**
     * Add experience and handle level ups.
     */
    public void addExperience(int amount) {
        this.xp += amount;
        checkLevelUp();
    }
    
    /**
     * Set callback for level up events.
     */
    public void setLevelUpCallback(LevelUpCallback callback) {
        this.levelUpCallback = callback;
    }
    
    private void checkLevelUp() {
        int newLevel = calculateLevel(this.xp);
        if (newLevel > this.level) {
            levelUp(newLevel);
            if (levelUpCallback != null) {
                levelUpCallback.onLevelUp(this);
            }
        }
    }
    
    private int calculateLevel(int xp) {
        for (int level = Constants.XP_THRESHOLDS.length - 1; level >= 0; level--) {
            if (xp >= Constants.XP_THRESHOLDS[level]) {
                return level + 1;
            }
        }
        return 1;
    }
    
    private void levelUp(int newLevel) {
        while (this.level < newLevel) {
            this.level++;
            applyLevelBonus(this.level);
        }
    }
    
    private void applyLevelBonus(int level) {
        // HP bonuses
        int hpBonus = 0;
        if (level <= 4) {
            hpBonus = Constants.HP_BONUS_EARLY;
        } else if (level == 10) {
            hpBonus = Constants.HP_BONUS_SPECIAL_10;
        } else if (level == 20) {
            hpBonus = Constants.HP_BONUS_SPECIAL_20;
        } else if (level <= 10) {
            hpBonus = Constants.HP_BONUS_MID_LOW;
        } else if (level <= 15) {
            hpBonus = Constants.HP_BONUS_MID_HIGH;
        } else if (level == 30) {
            hpBonus = Constants.HP_BONUS_MAX_LEVEL;
        } else if (level <= 20) {
            hpBonus = Constants.HP_BONUS_LATE_LOW;
        } else if (level <= 25) {
            hpBonus = Constants.HP_BONUS_LATE_HIGH;
        } else {
            hpBonus = Constants.HP_BONUS_END;
        }
        
        this.pv += hpBonus;
        this.maxPV += hpBonus;
        
        // Primary stat bonus based on class
        switch (type) {
            case WARRIOR:
                this.force += Constants.PRIMARY_STAT_BONUS;
                break;
            case ARCHER:
                this.agility += Constants.PRIMARY_STAT_BONUS;
                break;
            case MAGE:
                this.wisdom += Constants.PRIMARY_STAT_BONUS;
                break;
        }
        
        // Stat points to distribute
        this.unspentStatPoints += Constants.STAT_POINTS_PER_LEVEL;
    }
    
    /**
     * Spend stat points on a specific attribute.
     */
    public boolean spendStatPoints(String stat, int amount) {
        if (amount > unspentStatPoints || amount < 0) {
            return false;
        }
        
        switch (stat.toLowerCase()) {
            case "force":
                this.force += amount;
                break;
            case "agility":
            case "agilite":
                this.agility += amount;
                break;
            case "wisdom":
            case "sagesse":
                this.wisdom += amount;
                break;
            default:
                return false;
        }
        
        this.unspentStatPoints -= amount;
        return true;
    }
    
    // Getters
    public HeroType getType() { return type; }
    public int getPV() { return pv; }
    public int getMaxPV() { return maxPV; }
    public int getXP() { return xp; }
    public int getForce() { return force; }
    public int getAgility() { return agility; }
    public int getWisdom() { return wisdom; }
    public int getLevel() { return level; }
    public int getFloor() { return floor; }
    public int getUnspentStatPoints() { return unspentStatPoints; }
    public int getDamage() { return damage; }
    
    /**
     * Get total damage including base stat damage and equipment damage.
     * This is the actual damage value that should be displayed in UI.
     */
    public int getTotalDamage() {
        int statBasedDamage = 0;
        switch (type) {
            case WARRIOR:
                statBasedDamage = force;
                break;
            case ARCHER:
                statBasedDamage = agility;
                break;
            case MAGE:
                statBasedDamage = wisdom;
                break;
        }
        return damage + statBasedDamage;
    }
    
    public int getResistance() { return resistance; }
    public Direction getFacingDirection() { return facingDirection; }
    
    // Setters for combat stats
    public void setPV(int pv) { this.pv = pv; }
    public void setFloor(int floor) { this.floor = floor; }
    public void setDamage(int damage) { this.damage = damage; }
    public void setResistance(int resistance) { this.resistance = resistance; }
    public void setFacingDirection(Direction direction) { this.facingDirection = direction; }
    
    public void modifyPV(int amount) { this.pv += amount; }
    public void modifyForce(int amount) { this.force += amount; }
    public void modifyAgility(int amount) { this.agility += amount; }
    public void modifyWisdom(int amount) { this.wisdom += amount; }
    
    /**
     * Check if hero is alive.
     */
    public boolean isAlive() {
        return this.pv > 0;
    }
    
    /**
     * Callback interface for level up events.
     */
    public interface LevelUpCallback {
        void onLevelUp(Hero hero);
    }
    
    @Override
    public String toString() {
        return String.format("%s (Level %d) - PV:%d", 
                           type, level, pv);
    }
}