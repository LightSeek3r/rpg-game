package base;

import java.util.Random;

/**
 * Factory class for creating equipment items.
 */
public class EquipmentFactory {
    
    private static int nextId = 1;
    private static final Random random = new Random();
    
    /**
     * Generate a random rarity based on hero level.
     * As level increases, common items become less likely and rare items more likely.
     */
    public static Equipment.Rarity generateRarity(int heroLevel) {
        int roll = random.nextInt(100);
        
        // Scale factor: reduces common drop chance and increases rare drop chances
        // At level 0: no scaling
        // At level 30+: significant reduction in common, boost in rare+
        double scaleFactor = Math.min(heroLevel / 30.0, 1.0); // 0.0 to 1.0
        
        // Build a weighted list based on level scaling
        int[] adjustedChances = new int[Equipment.Rarity.values().length];
        
        for (int i = 0; i < Equipment.Rarity.values().length; i++) {
            Equipment.Rarity rarity = Equipment.Rarity.values()[i];
            
            // Skip if hero level is below minimum for this rarity
            if (heroLevel < rarity.getMinLevel()) {
                adjustedChances[i] = 0;
                continue;
            }
            
            int baseChance = rarity.getDropChance();
            
            // Adjust drop chances based on rarity tier
            if (rarity == Equipment.Rarity.COMMON) {
                // Common items: reduce by up to 70% at high levels
                adjustedChances[i] = (int)(baseChance * (1.0 - scaleFactor * 0.7));
            } else if (rarity == Equipment.Rarity.UNCOMMON) {
                // Uncommon: slight reduction at high levels
                adjustedChances[i] = (int)(baseChance * (1.0 - scaleFactor * 0.3));
            } else if (rarity == Equipment.Rarity.RARE || rarity == Equipment.Rarity.EPIC) {
                // Rare/Epic: significant boost at high levels
                adjustedChances[i] = (int)(baseChance * (1.0 + scaleFactor * 1.5));
            } else {
                // Legendary+: moderate boost at high levels
                adjustedChances[i] = (int)(baseChance * (1.0 + scaleFactor * 1.0));
            }
        }
        
        // Select rarity based on adjusted chances
        int cumulativeChance = 0;
        for (int i = 0; i < adjustedChances.length; i++) {
            cumulativeChance += adjustedChances[i];
            if (roll < cumulativeChance) {
                return Equipment.Rarity.values()[i];
            }
        }
        
        return Equipment.Rarity.COMMON;
    }
    
    /**
     * Create a weapon based on hero type and level.
     */
    public static Equipment createWeapon(Hero.HeroType heroType, int heroLevel, Equipment.Rarity rarity) {
        Equipment.EquipmentType weaponType;
        String name;
        
        switch (heroType) {
            case WARRIOR:
                weaponType = getRandomWarriorWeapon();
                name = getWeaponName(weaponType, rarity);
                break;
            case ARCHER:
                weaponType = getRandomArcherWeapon();
                name = getWeaponName(weaponType, rarity);
                break;
            case MAGE:
                weaponType = getRandomMageWeapon();
                name = getWeaponName(weaponType, rarity);
                break;
            default:
                weaponType = Equipment.EquipmentType.SWORD;
                name = "Epée de Base";
        }
        
        int baseDamage = 10 + (heroLevel * 2);
        int rarityMultiplier = getRarityMultiplier(rarity);
        
        return new Equipment.Builder(nextId++, name, weaponType, rarity)
                .damage(baseDamage * rarityMultiplier / 10)
                .description("Une arme " + rarity.getDisplayName().toLowerCase())
                .build();
    }
    
    /**
     * Create armor based on level and rarity.
     */
    public static Equipment createArmor(Equipment.EquipmentType armorType, int heroLevel, Equipment.Rarity rarity) {
        String name = getArmorName(armorType, rarity);
        int baseValue = 5 + heroLevel;
        int rarityMultiplier = getRarityMultiplier(rarity);
        
        Equipment.Builder builder = new Equipment.Builder(nextId++, name, armorType, rarity)
                .description("Une pièce d'armure " + rarity.getDisplayName().toLowerCase());
        
        switch (armorType) {
            case HEAD:
                builder.pv(baseValue * rarityMultiplier / 10)
                       .resistance(rarityMultiplier);
                break;
            case ARMOR:
                builder.pv(baseValue * 2 * rarityMultiplier / 10)
                       .resistance(rarityMultiplier * 2);
                break;
            case BELT:
                builder.pv(baseValue * rarityMultiplier / 10)
                       .statBonus(rarityMultiplier / 2);
                break;
            case BOOTS:
                builder.agility(rarityMultiplier);
                break;
            case OFF_HAND:
                builder.resistance(rarityMultiplier);
                break;
        }
        
        return builder.build();
    }
    
    /**
     * Create a potion that sets HP to current+5.
     */
    public static Equipment createPotion() {
        return new Equipment.Builder(nextId++, "Potion de Soin", Equipment.EquipmentType.POTION, Equipment.Rarity.COMMON)
                .pvRecovery(5)
                .description("Ajoute 5 PV")
                .build();
    }
    
    /**
     * Create a rune.
     */
    public static Equipment createRune(Equipment.EquipmentType runeType, int value) {
        String name = getRuneName(runeType);
        
        Equipment.Builder builder = new Equipment.Builder(nextId++, name, runeType, Equipment.Rarity.RARE)
                .description("Augmente de façon permanente " + name);
        
        switch (runeType) {
            case RUNE_AGILITY:
                builder.agility(value);
                break;
            case RUNE_FORCE:
                builder.force(value);
                break;
            case RUNE_WISDOM:
                builder.wisdom(value);
                break;
            case RUNE_PV:
                builder.pv(value);
                break;
            case RUNE_XP:
                builder.xp(value);
                break;
            case RUNE_GOLD:
                // Gold rune adds to ALL stats
                builder.force(value)
                       .agility(value)
                       .wisdom(value)
                       .pv(value * 5); // More PV since it's health
                builder.description("Augmente TOUTES les statistiques !");
                break;
        }
        
        return builder.build();
    }
    
    // Helper methods
    
    private static Equipment.EquipmentType getRandomWarriorWeapon() {
        Equipment.EquipmentType[] weapons = {
            Equipment.EquipmentType.SWORD,
            Equipment.EquipmentType.HAMMER,
            Equipment.EquipmentType.AXE
        };
        return weapons[random.nextInt(weapons.length)];
    }
    
    private static Equipment.EquipmentType getRandomArcherWeapon() {
        Equipment.EquipmentType[] weapons = {
            Equipment.EquipmentType.CROSSBOW,
            Equipment.EquipmentType.BOW,
            Equipment.EquipmentType.DAGGER
        };
        return weapons[random.nextInt(weapons.length)];
    }
    
    private static Equipment.EquipmentType getRandomMageWeapon() {
        Equipment.EquipmentType[] weapons = {
            Equipment.EquipmentType.WAND,
            Equipment.EquipmentType.STAFF,
            Equipment.EquipmentType.SCEPTER
        };
        return weapons[random.nextInt(weapons.length)];
    }
    
    private static int getRarityMultiplier(Equipment.Rarity rarity) {
        switch (rarity) {
            case COMMON: return 10;
            case UNCOMMON: return 12;
            case RARE: return 15;
            case EPIC: return 20;
            case LEGENDARY: return 25;
            case MYTHIC: return 30;
            case DIVINE: return 40;
            default: return 10;
        }
    }
    
    private static String getWeaponName(Equipment.EquipmentType type, Equipment.Rarity rarity) {
        String weaponBase = Equipment.getTypeDisplayName(type);
        String rarityAdjective = getRarityAdjective(rarity, type);
        return weaponBase + " " + rarityAdjective;
    }
    
    private static String getArmorName(Equipment.EquipmentType type, Equipment.Rarity rarity) {
        String armorBase = Equipment.getTypeDisplayName(type);
        String rarityAdjective = getRarityAdjective(rarity, type);
        return armorBase + " " + rarityAdjective;
    }
    
    private static String getRuneName(Equipment.EquipmentType type) {
        // Use the centralized display name from Equipment class
        return Equipment.getTypeDisplayName(type);
    }
    
    /**
     * Get the rarity adjective with proper French grammar agreement.
     * Handles gender (masculine/feminine) and number (singular/plural).
     */
    private static String getRarityAdjective(Equipment.Rarity rarity, Equipment.EquipmentType type) {
        // Determine if the item is feminine and/or plural
        boolean isFeminine = isFeminineItem(type);
        boolean isPlural = isPluralItem(type);
        
        switch (rarity) {
            case COMMON:
                if (isPlural) return isFeminine ? "Communes" : "Communs";
                return isFeminine ? "Commune" : "Commun";
            case UNCOMMON:
                return "De Qualité"; // Invariable
            case RARE:
                if (isPlural) return "Rares";
                return "Rare"; // Same for masculine/feminine
            case EPIC:
                if (isPlural) return "Épiques";
                return "Épique"; // Same for masculine/feminine
            case LEGENDARY:
                if (isPlural) return isFeminine ? "Légendaires" : "Légendaires";
                return isFeminine ? "Légendaire" : "Légendaire"; // Same for both
            case MYTHIC:
                if (isPlural) return "Mythiques";
                return "Mythique"; // Same for masculine/feminine
            case DIVINE:
                if (isPlural) return isFeminine ? "Divines" : "Divins";
                return isFeminine ? "Divine" : "Divin";
            default:
                if (isPlural) return "Basiques";
                return "Basique";
        }
    }
    
    /**
     * Determine if an item type is feminine in French.
     */
    private static boolean isFeminineItem(Equipment.EquipmentType type) {
        switch (type) {
            // Feminine items
            case WAND:        // Baguette
            case CROSSBOW:    // Arbalète
            case ARMOR:       // Armure
            case BELT:        // Ceinture
            case DAGGER:      // Dague
            case AXE:         // Hache
            case BOOTS:       // Bottes (feminine AND plural)
                return true;
            // Masculine items by default
            default:
                return false;
        }
    }
    
    /**
     * Determine if an item type is plural in French.
     */
    private static boolean isPluralItem(Equipment.EquipmentType type) {
        switch (type) {
            case BOOTS:  // Bottes (plural)
                return true;
            default:
                return false;
        }
    }
    
    private static String getRarityPrefix(Equipment.Rarity rarity) {
        switch (rarity) {
            case COMMON: return "Commune";
            case UNCOMMON: return "De Qualité";
            case RARE: return "Rare";
            case EPIC: return "Épique";
            case LEGENDARY: return "Légendaire";
            case MYTHIC: return "Mythique";
            case DIVINE: return "Divine";
            default: return "Basique";
        }
    }
}
