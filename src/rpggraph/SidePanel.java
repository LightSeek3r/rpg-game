package rpggraph;

import base.*;
import ihm.FrontEnd;
import ihm.model.Proprietes;

/**
 * Manages the side panel display that shows player stats, equipment,
 * combat information, and interactions.
 */
public class SidePanel {
    
    private final FrontEnd frontEnd;
    
    public SidePanel(FrontEnd frontEnd) {
        this.frontEnd = frontEnd;
    }
    
    /**
     * Update and display all side panel information.
     */
    public void update(GameManager gameManager) {
        Hero hero = gameManager.getHero();
        Inventaire inventory = gameManager.getInventory();
        CombatManager combatManager = gameManager.getCombatManager();
        
        // Clear all side panel content before adding new ones
        frontEnd.clearSidePanel();
        
        // Display player stats
        displayPlayerStats(hero, gameManager.getGameState());
        
        // Display attributes
        displayAttributes(hero);
        
        // Display equipped items
        displayEquippedItems(inventory);
        
        // Display combat info if in combat
        if (combatManager.isCombatActive()) {
            displayCombatInfo(combatManager, hero);
        } else {
            displayGameInfo(gameManager);
        }
        
        // Finalize the side panel layout
        frontEnd.finalizeSidePanel();
    }
    
    /**
     * Display player statistics.
     */
    private void displayPlayerStats(Hero hero, GameState gameState) {
        Proprietes stats = new Proprietes();
        
        stats.setDescriptionCategory("Player", gameState.getPlayerName());
        stats.ajouterStr("Player", "Classe", hero.getType().name());
        stats.ajouterInt("Player", "Niveau", hero.getLevel());
        stats.ajouterInt("Player", "Étage", hero.getFloor());
        
        // Calculate XP to next level
        int nextLevelXP = getNextLevelXP(hero.getLevel());
        if (nextLevelXP > 0) {
            stats.ajouterStr("Player", "Prochain Niveau", 
                String.format("%d / %d XP", hero.getXP(), nextLevelXP));
        }
        
        stats.ajouterInt("Player", "HP", hero.getPV());
        
        frontEnd.affiche(stats, true);
    }
    
    /**
     * Display character attributes.
     */
    private void displayAttributes(Hero hero) {
        Proprietes attrs = new Proprietes();
        
        attrs.setDescriptionCategory("Attributs", "Stats");
        attrs.ajouterInt("Attributs", "Force", hero.getForce());
        attrs.ajouterInt("Attributs", "Agilité", hero.getAgility());
        attrs.ajouterInt("Attributs", "Sagesse", hero.getWisdom());
        attrs.ajouterInt("Attributs", "Dégâts", hero.getTotalDamage());
        attrs.ajouterInt("Attributs", "Résistance", hero.getResistance());
        
        if (hero.getUnspentStatPoints() > 0) {
            attrs.ajouterInt("Attributs", "Points non dépensés", hero.getUnspentStatPoints());
        }
        
        frontEnd.affiche(attrs, true);
    }
    
    /**
     * Display equipped items.
     */
    private void displayEquippedItems(Inventaire inventory) {
        Proprietes equipped = new Proprietes();
        
        equipped.setDescriptionCategory("Équipement", "Objets équipés");
        
        // Head
        Equipment head = inventory.getEquippedItem(Equipment.EquipmentType.HEAD);
        equipped.ajouterStr("Équipement", "Tête", 
            head != null ? head.getName() : "-Vide-");
        
        // Armor
        Equipment armor = inventory.getEquippedItem(Equipment.EquipmentType.ARMOR);
        equipped.ajouterStr("Équipement", "Armure", 
            armor != null ? armor.getName() : "-Vide-");
        
        // Ceinture
        Equipment belt = inventory.getEquippedItem(Equipment.EquipmentType.BELT);
        equipped.ajouterStr("Équipement", "Ceinture", 
            belt != null ? belt.getName() : "-Vide-");
        
        // Bottes
        Equipment boots = inventory.getEquippedItem(Equipment.EquipmentType.BOOTS);
        equipped.ajouterStr("Équipement", "Bottes", 
            boots != null ? boots.getName() : "-Vide-");
        
        // Off-hand (Ring/Shield)
        Equipment offHand = inventory.getEquippedItem(Equipment.EquipmentType.OFF_HAND);
        equipped.ajouterStr("Équipement", "Anneau/Bouclier", 
            offHand != null ? offHand.getName() : "-Vide-");
        
        // Arme
        Equipment weapon = getEquippedWeapon(inventory);
        equipped.ajouterStr("Équipement", "Arme", 
            weapon != null ? weapon.getName() : "-Vide-");
        
        frontEnd.affiche(equipped, true);
    }
    
    /**
     * Get the currently equipped weapon.
     */
    private Equipment getEquippedWeapon(Inventaire inventory) {
        Equipment.EquipmentType[] weaponSlots = {
            Equipment.EquipmentType.SWORD,
            Equipment.EquipmentType.HAMMER,
            Equipment.EquipmentType.AXE,
            Equipment.EquipmentType.CROSSBOW,
            Equipment.EquipmentType.BOW,
            Equipment.EquipmentType.DAGGER,
            Equipment.EquipmentType.WAND,
            Equipment.EquipmentType.STAFF,
            Equipment.EquipmentType.SCEPTER
        };
        
        for (Equipment.EquipmentType slot : weaponSlots) {
            Equipment item = inventory.getEquippedItem(slot);
            if (item != null) {
                return item;
            }
        }
        
        return null;
    }
    
    /**
     * Display combat information.
     */
    private void displayCombatInfo(CombatManager combatManager, Hero hero) {
        Mob enemy = combatManager.getCurrentEnemy();
        if (enemy == null) return;
        
        Proprietes combat = new Proprietes();
        
        combat.setDescriptionCategory("COMBAT", "Bataille en cours !");
        combat.ajouterStr("COMBAT", "Ennemi", enemy.getType().name());
        combat.ajouterStr("COMBAT", "HP Ennemi", 
            String.format("%d / %d", enemy.getPV(), enemy.getMaxPV()));
        combat.ajouterStr("COMBAT", "HP Bar", 
            createHealthBar(enemy.getPV(), enemy.getMaxPV()));
        combat.ajouterInt("COMBAT", "Dégâts Ennemi", enemy.getDamage());
        
        combat.ajouterStr("COMBAT", "", "---");
        combat.ajouterStr("COMBAT", "Votre HP", 
            String.format("%d / %d", hero.getPV(), calculateMaxHP(hero)));
        combat.ajouterInt("COMBAT", "Vos Dégâts", hero.getTotalDamage());
        
        combat.ajouterStr("COMBAT", "Action", "Appuyez sur ESPACE pour attaquer !");
        
        frontEnd.affiche(combat, true);
    }
    
    /**
     * Display general game information.
     */
    private void displayGameInfo(GameManager gameManager) {
        Proprietes info = new Proprietes();
        
        GameState state = gameManager.getGameState();
        Level level = gameManager.getCurrentLevel();
        
        info.setDescriptionCategory("Level Info", "Palier " + state.getCurrentFloor());
        
        // Count remaining mobs
        int mobCount = level.getMobs().size();
        info.ajouterInt("Level Info", "Ennemis", mobCount);
        
        // Count remaining chests
        int chestCount = 0;
        for (Level.Chest chest : level.getChests().values()) {
            if (!chest.isOpened()) {
                chestCount++;
            }
        }
        info.ajouterInt("Level Info", "Coffres", chestCount);
        
        // Inventaire info
        Inventaire inventory = gameManager.getInventory();
        info.ajouterStr("Level Info", "Inventaire", 
            String.format("%d / %d", inventory.getSize(), inventory.getMaxSize()));
        
        info.ajouterStr("Level Info", "", "---");
        info.ajouterStr("Level Info", "Controls", "WASD/ZQSD: Bouger");
        info.ajouterStr("Level Info", "", "I: Inventaire");
        info.ajouterStr("Level Info", "", "P: Sauvegarder");
        
        frontEnd.affiche(info, true);
    }
    
    /**
     * Display chest opening results.
     * @return The message to display in the message bar.
     */
    public String displayChestContents(Level.Chest chest, boolean inventoryFull) {
        // Build a detailed message about chest contents
        StringBuilder message = new StringBuilder();
        message.append("Coffre ouvert ! Trouvé : ");
        
        int index = 0;
        for (Equipment item : chest.getItems()) {
            if (index > 0) {
                message.append(", ");
            }
            message.append(item.getName()).append(" (").append(item.getRarity().getDisplayName()).append(")");
            index++;
        }
        
        if (inventoryFull) {
            message.append(" - WARNING: Inventaire plein !");
        }
        
        return message.toString();
    }
    
    /**
     * Display level up information.
     * @return The message to display in the message bar.
     */
    public String displayLevelUp(Hero hero) {
        Proprietes levelUp = new Proprietes();
        
        levelUp.setDescriptionCategory("LEVEL UP!", "Félicitations !");
        levelUp.ajouterInt("LEVEL UP!", "Nouveau Niveau", hero.getLevel());
        levelUp.ajouterInt("LEVEL UP!", "HP", hero.getPV());
        
        if (hero.getUnspentStatPoints() > 0) {
            levelUp.ajouterInt("LEVEL UP!", "Stat Points", hero.getUnspentStatPoints());
            levelUp.ajouterStr("LEVEL UP!", "", "Appuyez sur 'C' pour les attribuer !");
        }
        
        frontEnd.affiche(levelUp, true);
        return "LEVEL UP! Vous êtes maintenant au niveau " + hero.getLevel();
    }
    
    /**
     * Create a visual health bar.
     */
    private String createHealthBar(int current, int max) {
        int barLength = 10;
        int filled = (int) ((double) current / max * barLength);
        
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < barLength; i++) {
            if (i < filled) {
                bar.append("=");
            } else {
                bar.append(" ");
            }
        }
        bar.append("]");
        
        return bar.toString();
    }
    
    /**
     * Get XP required for next level.
     */
    private int getNextLevelXP(int currentLevel) {
        if (currentLevel >= Constants.XP_THRESHOLDS.length) {
            return -1; // Max level
        }
        return Constants.XP_THRESHOLDS[currentLevel];
    }
    
    /**
     * Calculate maximum HP based on level progression.
     */
    private int calculateMaxHP(Hero hero) {
        // Return the hero's tracked maximum HP
        return hero.getMaxPV();
    }
}
