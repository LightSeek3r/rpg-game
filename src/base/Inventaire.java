package base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the hero's inventory and equipped items.
 */
public class Inventaire implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final int MAX_INVENTORY_SIZE = 84;
    
    private final Hero hero;
    private final List<Equipment> inventory;
    private final Map<Equipment.EquipmentType, Equipment> equippedItems;
    
    public Inventaire(Hero hero) {
        this.hero = hero;
        this.inventory = new ArrayList<>();
        this.equippedItems = new HashMap<>();
    }
    
    /**
     * Add an item to the inventory.
     * @return true if successfully added, false if inventory is full
     */
    public boolean addItem(Equipment item) {
        if (inventory.size() >= MAX_INVENTORY_SIZE) {
            return false;
        }
        
        // Check if item already exists in inventory
        for (Equipment existing : inventory) {
            if (existing.getId() == item.getId()) {
                existing.incrementQuantity();
                return true;
            }
        }
        
        item.setInInventory(true);
        item.setQuantity(1);
        inventory.add(item);
        return true;
    }
    
    /**
     * Remove an item from the inventory.
     */
    public boolean removeItem(Equipment item) {
        if (item.getQuantity() > 1) {
            item.decrementQuantity();
            return true;
        } else {
            item.setInInventory(false);
            return inventory.remove(item);
        }
    }
    
    /**
     * Equip an item from the inventory.
     */
    public boolean equipItem(Equipment item) {
        if (!inventory.contains(item)) {
            return false;
        }
        
        Equipment.EquipmentType slot = getEquipmentSlot(item.getType());
        if (slot == null) {
            // Item cannot be equipped (e.g., potions, runes)
            return false;
        }
        
        // Unequip current item in that slot if exists
        Equipment currentEquipped = equippedItems.get(slot);
        if (currentEquipped != null) {
            unequipItem(currentEquipped);
        }
        
        // Remove from inventory list and equip
        inventory.remove(item);
        item.setInInventory(false);
        item.setEquipped(true);
        equippedItems.put(slot, item);
        item.applyTo(hero);
        
        return true;
    }
    
    /**
     * Unequip an item.
     */
    public boolean unequipItem(Equipment item) {
        if (!item.isEquipped()) {
            return false;
        }
        
        Equipment.EquipmentType slot = getEquipmentSlot(item.getType());
        if (slot != null && equippedItems.get(slot) == item) {
            // Check if there's space in inventory
            if (inventory.size() >= MAX_INVENTORY_SIZE) {
                return false; // Cannot unequip - inventory full
            }
            
            item.setEquipped(false);
            equippedItems.remove(slot);
            item.removeFrom(hero);
            
            // Add back to inventory
            item.setInInventory(true);
            inventory.add(item);
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Use a consumable item (potion or rune).
     */
    public boolean useItem(Equipment item) {
        if (!inventory.contains(item)) {
            return false;
        }
        
        // Apply item effects
        item.applyTo(hero);
        
        // Remove consumable from inventory
        removeItem(item);
        
        return true;
    }
    
    /**
     * Get the equipment slot for an equipment type.
     */
    private Equipment.EquipmentType getEquipmentSlot(Equipment.EquipmentType type) {
        switch (type) {
            case HEAD:
            case ARMOR:
            case BELT:
            case BOOTS:
            case OFF_HAND:
            case SWORD:
            case HAMMER:
            case AXE:
            case CROSSBOW:
            case BOW:
            case DAGGER:
            case WAND:
            case STAFF:
            case SCEPTER:
                return type;
            default:
                return null;
        }
    }
    
    /**
     * Get all items in inventory.
     */
    public List<Equipment> getInventory() {
        return new ArrayList<>(inventory);
    }
    
    /**
     * Get equipped item in a specific slot.
     */
    public Equipment getEquippedItem(Equipment.EquipmentType slot) {
        return equippedItems.get(slot);
    }
    
    /**
     * Get all equipped items.
     */
    public Map<Equipment.EquipmentType, Equipment> getEquippedItems() {
        return new HashMap<>(equippedItems);
    }
    
    /**
     * Check if inventory is full.
     */
    public boolean isFull() {
        return inventory.size() >= MAX_INVENTORY_SIZE;
    }
    
    /**
     * Get current inventory size.
     */
    public int getSize() {
        return inventory.size();
    }
    
    /**
     * Get maximum inventory size.
     */
    public int getMaxSize() {
        return MAX_INVENTORY_SIZE;
    }
}
