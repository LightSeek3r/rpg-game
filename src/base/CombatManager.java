package base;

/**
 * Manages combat between hero and mobs.
 */
public class CombatManager {
    
    private final Hero hero;
    private Mob currentEnemy;
    private boolean combatActive;
    private int enemyX;
    private int enemyY;
    private CombatStartCallback combatStartCallback;
    
    public CombatManager(Hero hero) {
        this.hero = hero;
        this.combatActive = false;
    }
    
    /**
     * Callback interface for combat start events.
     */
    public interface CombatStartCallback {
        void onCombatStart(Mob enemy);
    }
    
    /**
     * Set callback for combat start events.
     */
    public void setCombatStartCallback(CombatStartCallback callback) {
        this.combatStartCallback = callback;
    }
    
    /**
     * Start combat with a mob.
     */
    public void startCombat(Mob mob, int x, int y) {
        this.currentEnemy = mob;
        this.enemyX = x;
        this.enemyY = y;
        this.combatActive = true;
        System.out.println("Combat started against " + mob);
        
        // Notify callback if set
        if (combatStartCallback != null) {
            combatStartCallback.onCombatStart(mob);
        }
    }
    
    /**
     * Hero attacks the current enemy.
     */
    public CombatResult heroAttack() {
        if (!combatActive || currentEnemy == null) {
            return new CombatResult(false, "Aucun ennemi à attaquer");
        }
        
        // Calculate total damage (equipment + stat-based damage)
        int totalDamage = hero.getTotalDamage();
        currentEnemy.takeDamage(totalDamage);
        
        // Check if mob is defeated
        if (!currentEnemy.isAlive()) {
            return defeatEnemy();
        }
        
        return new CombatResult(true, 
                String.format("Vous infligez %d dégâts à %s", totalDamage, currentEnemy.getType()));
    }
    
    /**
     * Enemy attacks the hero.
     */
    public CombatResult enemyTurn() {
        if (!combatActive || currentEnemy == null) {
            return new CombatResult(false, "Aucun combat actif");
        }
        
        // Enemy attacks once per turn
        currentEnemy.attackHero(hero);
        String result = String.format("%s attaque pour %d dégâts.", 
                currentEnemy.getType(), currentEnemy.getDamage());
        
        // Check if hero died
        if (!hero.isAlive()) {
            combatActive = false;
            result += " Vous avez été vaincu !";
            return new CombatResult(false, result);
        }
        
        return new CombatResult(true, result);
    }
    
    /**
     * Handle mob defeat.
     */
    private CombatResult defeatEnemy() {
        int xpReward = currentEnemy.getXPReward();
        hero.addExperience(xpReward);
        
        String message = String.format("Vous avez vaincu %s et gagné %d XP !", 
                currentEnemy.getType(), xpReward);
        
        endCombat();
        return new CombatResult(true, message);
    }
    
    /**
     * End the current combat.
     */
    public void endCombat() {
        this.combatActive = false;
        this.currentEnemy = null;
    }
    
    /**
     * Check if combat is active.
     */
    public boolean isCombatActive() {
        return combatActive;
    }
    
    /**
     * Get the current enemy.
     */
    public Mob getCurrentEnemy() {
        return currentEnemy;
    }
    
    /**
     * Get the current enemy X position.
     */
    public int getEnemyX() {
        return enemyX;
    }
    
    /**
     * Get the current enemy Y position.
     */
    public int getEnemyY() {
        return enemyY;
    }
    
    /**
     * Result of a combat action.
     */
    public static class CombatResult {
        private final boolean success;
        private final String message;
        
        public CombatResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
