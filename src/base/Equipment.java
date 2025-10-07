package base;

import java.io.Serializable;

/**
 * Represents equipment items in the game.
 */
public class Equipment implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public enum EquipmentType {
        HEAD, OFF_HAND, POTION, ARMOR, BELT, BOOTS,
        RUNE_AGILITY, RUNE_FORCE, RUNE_GOLD, RUNE_PV, RUNE_WISDOM, RUNE_XP,
        SWORD, HAMMER, AXE,
        CROSSBOW, BOW, DAGGER,
        WAND, STAFF, SCEPTER
    }
    
    public enum Rarity {
        COMMON(30, 1),      // 30% drop, level 1
        UNCOMMON(25, 1),    // 25% drop, level 1
        RARE(20, 5),        // 20% drop, level 5
        EPIC(10, 15),       // 10% drop, level 15
        LEGENDARY(8, 20),   // 8% drop, level 20
        MYTHIC(5, 22),      // 5% drop, level 22
        DIVINE(2, 25);      // 2% drop, level 25
        
        private final int dropChance;
        private final int minLevel;
        
        Rarity(int dropChance, int minLevel) {
            this.dropChance = dropChance;
            this.minLevel = minLevel;
        }
        
        public int getDropChance() { return dropChance; }
        public int getMinLevel() { return minLevel; }
        
        /**
         * Get the French display name for this rarity.
         */
        public String getDisplayName() {
            switch (this) {
                case COMMON: return "Commun";
                case UNCOMMON: return "Peu Commun";
                case RARE: return "Rare";
                case EPIC: return "Épique";
                case LEGENDARY: return "Légendaire";
                case MYTHIC: return "Mythique";
                case DIVINE: return "Divin";
                default: return this.name();
            }
        }
    }
    
    private final int id;
    private final String name;
    private final String description;
    private final EquipmentType type;
    private final Rarity rarity;
    
    // Combat stats
    private final int damage;
    private final int pvRecovery;
    
    // Base stats
    private final int pv;
    
    // Bonuses
    private final int damageBonus;
    private final int statBonus;
    private final int resistance;
    private final int force;
    private final int agility;
    private final int wisdom;
    private final int xp;
    
    // State
    private boolean inInventory;
    private boolean equipped;
    private int quantity;
    
    private Equipment(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.type = builder.type;
        this.rarity = builder.rarity;
        this.damage = builder.damage;
        this.pvRecovery = builder.pvRecovery;
        this.pv = builder.pv;
        this.damageBonus = builder.damageBonus;
        this.statBonus = builder.statBonus;
        this.resistance = builder.resistance;
        this.force = builder.force;
        this.agility = builder.agility;
        this.wisdom = builder.wisdom;
        this.xp = builder.xp;
        this.inInventory = false;
        this.equipped = false;
        this.quantity = 0;
    }
    
    public static class Builder {
        private final int id;
        private final String name;
        private final EquipmentType type;
        private final Rarity rarity;
        
        private String description = "";
        private int damage = 0;
        private int pvRecovery = 0;
        private int pv = 0;
        private int damageBonus = 0;
        private int statBonus = 0;
        private int resistance = 0;
        private int force = 0;
        private int agility = 0;
        private int wisdom = 0;
        private int xp = 0;
        
        public Builder(int id, String name, EquipmentType type, Rarity rarity) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.rarity = rarity;
        }
        
        public Builder description(String desc) { this.description = desc; return this; }
        public Builder damage(int val) { this.damage = val; return this; }
        public Builder pvRecovery(int val) { this.pvRecovery = val; return this; }
        public Builder pv(int val) { this.pv = val; return this; }
        public Builder damageBonus(int val) { this.damageBonus = val; return this; }
        public Builder statBonus(int val) { this.statBonus = val; return this; }
        public Builder resistance(int val) { this.resistance = val; return this; }
        public Builder force(int val) { this.force = val; return this; }
        public Builder agility(int val) { this.agility = val; return this; }
        public Builder wisdom(int val) { this.wisdom = val; return this; }
        public Builder xp(int val) { this.xp = val; return this; }
        
        public Equipment build() {
            return new Equipment(this);
        }
    }
    
    /**
     * Apply equipment stats to a hero.
     */
    public void applyTo(Hero hero) {
        // For potions and consumables, add 5 HP
        if (this.type == EquipmentType.POTION) {
            hero.modifyPV(5);
            // Potions don't modify other stats, return early
            return;
        }
        
        // Apply permanent stat increases (for runes and equipment)
        hero.modifyPV(this.pv);
        hero.addExperience(this.xp);
        hero.modifyForce(this.force + this.statBonus);
        hero.modifyAgility(this.agility + this.statBonus);
        hero.modifyWisdom(this.wisdom + this.statBonus);
        
        // Damage calculation based on hero type
        int totalDamage = this.damage + this.damageBonus;
        if (hero.getType() == Hero.HeroType.WARRIOR) {
            totalDamage += this.force;
        } else if (hero.getType() == Hero.HeroType.ARCHER) {
            totalDamage += this.agility;
        } else if (hero.getType() == Hero.HeroType.MAGE) {
            totalDamage += this.wisdom;
        }
        hero.setDamage(hero.getDamage() + totalDamage);
        
        hero.setResistance(hero.getResistance() + this.resistance);
    }
    
    /**
     * Remove equipment stats from a hero.
     */
    public void removeFrom(Hero hero) {
        hero.modifyPV(-this.pv);
        hero.modifyForce(-(this.force + this.statBonus));
        hero.modifyAgility(-(this.agility + this.statBonus));
        hero.modifyWisdom(-(this.wisdom + this.statBonus));
        
        int totalDamage = this.damage + this.damageBonus;
        if (hero.getType() == Hero.HeroType.WARRIOR) {
            totalDamage += this.force;
        } else if (hero.getType() == Hero.HeroType.ARCHER) {
            totalDamage += this.agility;
        } else if (hero.getType() == Hero.HeroType.MAGE) {
            totalDamage += this.wisdom;
        }
        hero.setDamage(hero.getDamage() - totalDamage);
        
        hero.setResistance(hero.getResistance() - this.resistance);
    }
    
    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public EquipmentType getType() { return type; }
    public Rarity getRarity() { return rarity; }
    public boolean isEquipped() { return equipped; }
    
    /**
     * Get the French display name for an equipment type.
     */
    public static String getTypeDisplayName(EquipmentType type) {
        switch (type) {
            case HEAD: return "Casque";
            case OFF_HAND: return "Anneau/Bouclier";
            case POTION: return "Potion";
            case ARMOR: return "Armure";
            case BELT: return "Ceinture";
            case BOOTS: return "Bottes";
            case RUNE_AGILITY: return "Rune d'Agilité";
            case RUNE_FORCE: return "Rune de Force";
            case RUNE_GOLD: return "Rune d'Or";
            case RUNE_PV: return "Rune de PV";
            case RUNE_WISDOM: return "Rune de Sagesse";
            case RUNE_XP: return "Rune d'XP";
            case SWORD: return "Épée";
            case HAMMER: return "Marteau";
            case AXE: return "Hache";
            case CROSSBOW: return "Arbalète";
            case BOW: return "Arc";
            case DAGGER: return "Dague";
            case WAND: return "Baguette";
            case STAFF: return "Bâton";
            case SCEPTER: return "Sceptre";
            default: return type.name();
        }
    }
    public boolean isInInventory() { return inInventory; }
    public int getQuantity() { return quantity; }
    
    // Setters for state
    public void setEquipped(boolean equipped) { this.equipped = equipped; }
    public void setInInventory(boolean inInventory) { this.inInventory = inInventory; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void incrementQuantity() { this.quantity++; }
    public void decrementQuantity() { if (this.quantity > 0) this.quantity--; }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - Level %d+", 
                           name, rarity, rarity.getMinLevel());
    }
}