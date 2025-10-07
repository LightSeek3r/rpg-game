package rpggraph;

import base.*;
import ihm.FrontEnd;
import ihm.controller.Evenement;
import ihm.model.Proprietes;

/**
 * Inventory display system.
 */
public class InventoryUI {
    
    private final FrontEnd frontEnd;
    private final String imageDir;
    
    public InventoryUI(FrontEnd frontEnd) {
        this.frontEnd = frontEnd;
        this.imageDir = Constants.IMAGE_DIR;
    }
    
    /**
     * Display the inventory screen.
     */
    public void displayInventory(GameState gameState) {
        Hero hero = gameState.getHero();
        Inventaire inventory = gameState.getInventory();
        
        // Display hero stats panel
        displayHeroStats(hero, gameState.getPlayerName());
        
        // Display equipped items panel
        displayEquippedItems(inventory);
        
        // Display inventory grid
        displayInventoryGrid(inventory);
        
        // Display selected item details (if any)
        displayItemDetails(null); // Initially no item selected
        
        // Force display update to ensure all changes are rendered
        frontEnd.updateDisplay();
    }
    
    /**
     * Display hero statistics.
     */
    private void displayHeroStats(Hero hero, String playerName) {
        Proprietes stats = new Proprietes();
        
        stats.setDescriptionCategory("Hero Stats", playerName);
        stats.ajouterStr("Class", "Type", hero.getType().name());
        stats.ajouterInt("Hero Stats", "Level", hero.getLevel());
        stats.ajouterInt("Hero Stats", "XP", hero.getXP());
        stats.ajouterInt("Hero Stats", "HP", hero.getPV());
        stats.ajouterInt("Hero Stats", "Force", hero.getForce());
        stats.ajouterInt("Hero Stats", "Agilité", hero.getAgility());
        stats.ajouterInt("Hero Stats", "Sagesse", hero.getWisdom());
        stats.ajouterInt("Hero Stats", "Dégâts", hero.getTotalDamage());
        stats.ajouterInt("Hero Stats", "Résistance", hero.getResistance());
        
        if (hero.getUnspentStatPoints() > 0) {
            stats.ajouterInt("Hero Stats", "Points non dépensés", hero.getUnspentStatPoints());
        }
        
        frontEnd.affiche(stats, true);
    }
    
    /**
     * Display currently equipped items.
     */
    private void displayEquippedItems(Inventaire inventory) {
        Proprietes equipped = new Proprietes();
        
        equipped.setDescriptionCategory("Equipped", "Équipement Actuel");
        
        // Display each equipment slot
        addEquipmentSlot(equipped, inventory, Equipment.EquipmentType.HEAD, "Tête");
        addEquipmentSlot(equipped, inventory, Equipment.EquipmentType.ARMOR, "Armure");
        addEquipmentSlot(equipped, inventory, Equipment.EquipmentType.BELT, "Ceinture");
        addEquipmentSlot(equipped, inventory, Equipment.EquipmentType.BOOTS, "Bottes");
        addEquipmentSlot(equipped, inventory, Equipment.EquipmentType.OFF_HAND, "Anneau/Bouclier");
        
        // Display weapon (need to check multiple weapon types)
        Equipment weapon = getEquippedWeapon(inventory);
        if (weapon != null) {
            equipped.ajouterStr("Equipped", "Arme", weapon.getName());
        } else {
            equipped.ajouterStr("Equipped", "Arme", "Aucune");
        }
        
        frontEnd.affiche(equipped, true);
    }
    
    /**
     * Add an equipment slot to the properties.
     */
    private void addEquipmentSlot(Proprietes props, Inventaire inventory, 
                                   Equipment.EquipmentType slot, String displayName) {
        Equipment item = inventory.getEquippedItem(slot);
        String value = (item != null) ? item.getName() : "Aucun";
        props.ajouterStr("Equipped", displayName, value);
    }
    
    /**
     * Get the equipped weapon (checks all weapon slots).
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
     * Display inventory grid with items.
     */
    private void displayInventoryGrid(Inventaire inventory) {
        // Create grid positions array
        int[][] gridPositions = createInventoryGrid();
        
        // First, clear all slots by drawing background
        for (int i = 0; i < gridPositions.length; i++) {
            int x = gridPositions[i][0];
            int y = gridPositions[i][1];
            frontEnd.setBackground(x, y, imageDir + "BackInv.png");
        }
        
        // Fill empty spaces
        for (int y = 0; y < Constants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
                if (x % 2 == 0 || y % 2 == 0) {
                    frontEnd.setBackground(x, y, imageDir + "Sol.png");
                }
            }
        }
        
        // Display items in grid
        int index = 0;
        
        for (Equipment item : inventory.getInventory()) {
            if (index >= gridPositions.length) {
                break; // Inventory full
            }
            
            int x = gridPositions[index][0];
            int y = gridPositions[index][1];
            
            displayItemInSlot(x, y, item);
            index++;
        }
    }
    
    /**
     * Create inventory grid positions.
     */
    private int[][] createInventoryGrid() {
        int[][] positions = new int[Constants.INVENTORY_GRID_COLS * Constants.INVENTORY_GRID_ROWS][2];
        int index = 0;
        
        for (int row = 0; row < Constants.INVENTORY_GRID_ROWS; row++) {
            for (int col = 0; col < Constants.INVENTORY_GRID_COLS; col++) {
                positions[index][0] = 1 + (col * 2); // x position
                positions[index][1] = 1 + (row * 2); // y position
                index++;
            }
        }
        
        return positions;
    }
    
    /**
     * Display an item in a specific inventory slot.
     */
    private void displayItemInSlot(int x, int y, Equipment item) {
        String image = getItemImage(item);
        frontEnd.setBackground(x, y, imageDir + image);
        
        // If equipped, show indicator
        if (item.isEquipped()) {
            // Could overlay a small indicator
        }
        
        // If quantity > 1, could show number
        if (item.getQuantity() > 1) {
            // Could display quantity text
        }
    }
    
    /**
     * Get the image filename for an item.
     */
    private String getItemImage(Equipment item) {
        switch (item.getType()) {
            case HEAD:
                return "Casque.png";
            case OFF_HAND:
                return "Anneau.png";
            case POTION:
                return "Potion.png";
            case ARMOR:
                return "Armure.png";
            case BELT:
                return "Ceinture.png";
            case BOOTS:
                return "Bottes.png";
            case RUNE_AGILITY:
                return "RuneAgili.png";
            case RUNE_FORCE:
                return "RuneForce.png";
            case RUNE_GOLD:
                return "RuneOr.png";
            case RUNE_PV:
                return "RunePv.png";
            case RUNE_WISDOM:
                return "RuneSagesse.png";
            case RUNE_XP:
                return "RuneXp.png";
            case SWORD:
                return "Sabre.png";
            case HAMMER:
                return "Marteau.png";
            case AXE:
                return "Hache.png";
            case CROSSBOW:
                return "Arbalette.png";
            case BOW:
                return "Arc.png";
            case DAGGER:
                return "Dague.png";
            case WAND:
                return "Baguette.png";
            case STAFF:
                return "Baton.png";
            case SCEPTER:
                return "Sceptre.png";
            default:
                return "Sol.png";
        }
    }
    
    /**
     * Display details of a selected item.
     */
    private void displayItemDetails(Equipment item) {
        if (item == null) {
            // Display empty panel
            Proprietes details = new Proprietes();
            details.setDescriptionCategory("Item", "Pas d'objet sélectionné");
            frontEnd.affiche(details, false);
            return;
        }
        
        Proprietes details = new Proprietes();
        details.setDescriptionCategory("Item Details", item.getName());
        details.ajouterStr("Item Details", "Type", Equipment.getTypeDisplayName(item.getType()));
        details.ajouterStr("Item Details", "Rareté", item.getRarity().getDisplayName());
        details.ajouterStr("Item Details", "Description", item.getDescription());
        
        if (item.getQuantity() > 1) {
            details.ajouterInt("Item Details", "Quantité", item.getQuantity());
        }
        
        frontEnd.affiche(details, false);
    }
    
    /**
     * Handle click on inventory slot.
     */
    public Equipment handleInventoryClick(int x, int y, Inventaire inventory) {
        int[][] gridPositions = createInventoryGrid();
        
        // Find which slot was clicked
        for (int i = 0; i < gridPositions.length; i++) {
            if (gridPositions[i][0] == x && gridPositions[i][1] == y) {
                // Get item at this index
                if (i < inventory.getInventory().size()) {
                    return inventory.getInventory().get(i);
                }
                break;
            }
        }
        
        return null;
    }
}
