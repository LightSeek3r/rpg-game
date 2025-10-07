package base;

/**
 * Represents monsters/mobs in the game.
 */
public class Mob {
    
    public enum MobType {
        SMALL,   // Base stats
        LARGE,   // 2x HP, same damage as small
        BOSS     // 2x HP, 1.5x damage compared to small
    }
    
    private final MobType type;
    private final int floor;
    private int pv;
    private final int maxPV;
    private final int damage;
    
    private Mob(MobType type, int floor, int pv, int damage) {
        this.type = type;
        this.floor = floor;
        this.pv = pv;
        this.maxPV = pv;
        this.damage = damage;
    }
    
    /**
     * Factory method to create mobs based on type and floor.
     */
    public static Mob createMob(MobType type, int floor) {
        switch (type) {
            case SMALL:
                return createSmallMob(floor);
            case LARGE:
                return createLargeMob(floor);
            case BOSS:
                return createBoss(floor);
            default:
                throw new IllegalArgumentException("Unknown mob type");
        }
    }
    
    private static Mob createSmallMob(int floor) {
        int pv, damage;
        
        switch (floor) {
            case 0: pv = 100; damage = 15; break;
            case 1: pv = 200; damage = 30; break;
            case 2: pv = 300; damage = 50; break;
            case 3: pv = 400; damage = 100; break;
            case 4: pv = 500; damage = 200; break;
            case 5: pv = 600; damage = 300; break;
            case 6: pv = 700; damage = 500; break;
            case 7: pv = 800; damage = 600; break;
            case 8: pv = 900; damage = 800; break;
            case 9: pv = 1000; damage = 1000; break;
            default: pv = 50; damage = 15;
        }
        
        return new Mob(MobType.SMALL, floor, pv, damage);
    }
    
    private static Mob createLargeMob(int floor) {
        // Large mobs have 2x HP and same damage as small mobs
        Mob smallMob = createSmallMob(floor);
        return new Mob(MobType.LARGE, floor, smallMob.maxPV * 2, smallMob.damage);
    }
    
    private static Mob createBoss(int floor) {
        // Boss mobs have 2x HP and 1.5x damage compared to small mobs
        Mob smallMob = createSmallMob(floor);
        return new Mob(MobType.BOSS, floor, smallMob.maxPV * 3, (int)(smallMob.damage * 2));
    }
    
    /**
     * Perform a basic attack on the hero.
     */
    public void attackHero(Hero hero) {
        int actualDamage;
        
        // Bosses ignore resistance based on floor level
        if (this.type == MobType.BOSS) {
            int resistanceIgnored = (this.floor + 1) * 10;
            int effectiveResistance = hero.getResistance() - resistanceIgnored;
            if (effectiveResistance < 0) {
                effectiveResistance = 0;
            }
            actualDamage = this.damage - effectiveResistance;
            if (actualDamage < 0) {
                actualDamage = 0;
            }
        } else {
            // Regular mobs: damage reduced by resistance (flat damage absorption)
            actualDamage = this.damage - hero.getResistance();
            if (actualDamage < 0) {
                actualDamage = 0; // Resistance can't cause negative damage (healing)
            }
        }
        
        hero.modifyPV(-actualDamage);
    }
    
    /**
     * Take damage from an attack.
     */
    public void takeDamage(int damage) {
        this.pv -= damage;
        if (this.pv < 0) {
            this.pv = 0;
        }
    }
    
    /**
     * Check if mob is alive.
     */
    public boolean isAlive() {
        return this.pv > 0;
    }
    
    /**
     * Get XP reward for defeating this mob.
     */
    public int getXPReward() {
        // XP scales with floor and mob type
        int baseXP = 50 + (floor * 10);
        switch (type) {
            case SMALL:
                return baseXP;
            case LARGE:
                return baseXP * 2;
            case BOSS:
                return baseXP * 5;
            default:
                return baseXP;
        }
    }
    
    // Getters
    public MobType getType() { return type; }
    public int getFloor() { return floor; }
    public int getPV() { return pv; }
    public int getMaxPV() { return maxPV; }
    public int getDamage() { return damage; }
    
    // Setters
    public void setPV(int pv) { this.pv = pv; }
    
    @Override
    public String toString() {
        return String.format("%s Monstre (Etage %d) - PV: %d/%d", 
                           type, floor, pv, maxPV);
    }
}