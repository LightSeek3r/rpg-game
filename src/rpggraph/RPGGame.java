package rpggraph;

import base.*;
import ihm.FrontEnd;
import ihm.controller.Evenement;

/**
 * Main game class that ties all systems together.
 */
public class RPGGame {
    
    private GameManager gameManager;
    private FrontEnd frontEnd;
    private GameRenderer renderer;
    private InventoryUI inventoryUI;
    private SidePanel sidePanel;
    private MessageBar messageBar;
    private boolean showingInventory;
    
    public RPGGame(GameState gameState) {
        this.gameManager = new GameManager(gameState);
        this.frontEnd = new FrontEnd(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);
        this.renderer = new GameRenderer(frontEnd);
        this.inventoryUI = new InventoryUI(frontEnd);
        this.sidePanel = new SidePanel(frontEnd);
        this.messageBar = new MessageBar(frontEnd);
        this.showingInventory = false;
        
        // Set up callbacks
        gameManager.setChestOpenCallback((chest, inventoryFull) -> {
            String msg = sidePanel.displayChestContents(chest, inventoryFull);
            messageBar.setMessage(msg);
            
            // Show popup with chest contents
            StringBuilder popupMessage = new StringBuilder();
            popupMessage.append("Coffre ouvert !\n\n");
            popupMessage.append("Objets trouvés :\n");
            
            for (Equipment item : chest.getItems()) {
                popupMessage.append("• ")
                    .append(item.getName())
                    .append(" (")
                    .append(item.getRarity().getDisplayName())
                    .append(")\n");
            }
            
            if (inventoryFull) {
                popupMessage.append("\n⚠ ATTENTION : Inventaire plein !\n");
                popupMessage.append("Certains objets n'ont pas pu être ajoutés.");
            }
            
            javax.swing.JOptionPane.showMessageDialog(
                null,
                popupMessage.toString(),
                "Coffre",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );
        });
        
        gameManager.getCombatManager().setCombatStartCallback((enemy) -> {
            // Show popup when combat starts
            String enemyName = getEnemyDisplayName(enemy.getType());
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "⚔️ COMBAT ! ⚔️\n\n" +
                "Vous affrontez : " + enemyName + "\n" +
                "HP Ennemi : " + enemy.getPV() + " / " + enemy.getMaxPV() + "\n" +
                "Dégâts : " + enemy.getDamage() + "\n\n" +
                "Appuyez sur ESPACE pour attaquer !",
                "Début du Combat",
                javax.swing.JOptionPane.WARNING_MESSAGE
            );
        });
        
        gameManager.getHero().setLevelUpCallback((hero) -> {
            String msg = sidePanel.displayLevelUp(hero);
            messageBar.setMessage(msg);
            
            // Show popup for level up with gained XP info
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "LEVEL UP !\n\n" +
                "Félicitations ! Vous êtes maintenant niveau " + hero.getLevel() + " !\n" +
                "XP actuel : " + hero.getXP() + "\n\n" +
                "Vous avez gagné " + Constants.STAT_POINTS_PER_LEVEL + " points de caractéristiques à dépenser.\n" +
                "Appuyez sur 'C' pour les attribuer.",
                "Montée de niveau",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );
        });
        
        System.out.println("Game started for " + gameState.getPlayerName());
        System.out.println("Hero: " + gameState.getHero().getType());
    }
    
    /**
     * Main game loop.
     */
    public void run() {
        // Wait a bit for the window to be fully initialized
        // We can't use invokeAndWait here because we might already be on the EDT
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // Ignore
        }
        
        // Initial render
        renderer.render(gameManager);
        sidePanel.update(gameManager);
        
        // Game loop
        while (!gameManager.isGameOver() && !gameManager.isGameWon()) {
            // Wait for player input
            Evenement event = frontEnd.lireEvenement();
            
            if (event.isMouseEvent()) {
                handleMouseEvent(event);
            } else if (event.isKeyboardEvent()) {
                handleKeyboardEvent(event);
            }
            
            // Re-render
            if (!showingInventory) {
                renderer.render(gameManager);
                sidePanel.update(gameManager);
            }
            
            // Check game state
            if (gameManager.isGameOver()) {
                gameOver();
                break;
            }
            
            if (gameManager.isGameWon()) {
                gameWon();
                break;
            }
        }
    }
    
    /**
     * Handle mouse events.
     */
    private void handleMouseEvent(Evenement event) {
        int x = event.getXPosition();
        int y = event.getYPosition();
        
        if (showingInventory) {
            // Handle inventory clicks
            Equipment item = inventoryUI.handleInventoryClick(x, y, gameManager.getInventory());
            if (item != null) {
                handleItemClick(item);
            }
        } else {
            // Handle game board clicks
            System.out.println("Clicked position: (" + x + ", " + y + ")");
        }
    }
    
    /**
     * Handle keyboard events.
     */
    private void handleKeyboardEvent(Evenement event) {
        char key = event.getKey();
        int keyCode = event.getKeyCode();
        
        // Check for arrow keys first (using key codes)
        if (keyCode == java.awt.event.KeyEvent.VK_UP) {
            movePlayer(GameManager.Direction.UP);
            return;
        } else if (keyCode == java.awt.event.KeyEvent.VK_DOWN) {
            movePlayer(GameManager.Direction.DOWN);
            return;
        } else if (keyCode == java.awt.event.KeyEvent.VK_LEFT) {
            movePlayer(GameManager.Direction.LEFT);
            return;
        } else if (keyCode == java.awt.event.KeyEvent.VK_RIGHT) {
            movePlayer(GameManager.Direction.RIGHT);
            return;
        }
        
        // Handle letter keys
        switch (Character.toLowerCase(key)) {
            // Movement
            case 'z': // Up
            case 'w':
                movePlayer(GameManager.Direction.UP);
                break;
            case 's': // Down
                movePlayer(GameManager.Direction.DOWN);
                break;
            case 'q': // Left
            case 'a':
                movePlayer(GameManager.Direction.LEFT);
                break;
            case 'd': // Right
                movePlayer(GameManager.Direction.RIGHT);
                break;
                
            // Combat
            case ' ': // Space - Attack
                if (gameManager.getCombatManager().isCombatActive()) {
                    attack();
                }
                break;
                
            // Inventory
            case 'i':
                toggleInventory();
                break;
                
            // Attribute stat points
            case 'c': // C for character stats
                openStatPointDialog();
                break;
                
            // Save
            case 'p': // P for pause/save
                saveGame();
                break;
                
            // Quit
            case 'x':
                if (confirmQuit()) {
                    System.exit(0);
                }
                break;
                
            default:
                System.out.println("Unknown key: " + key);
        }
        
        // Handle special keys (ESC)
        if (keyCode == java.awt.event.KeyEvent.VK_ESCAPE) {
            handleEscapeKey();
        }
    }
    
    /**
     * Move the player.
     */
    private void movePlayer(GameManager.Direction direction) {
        if (gameManager.getCombatManager().isCombatActive()) {
            messageBar.setMessage("Déplacement impossible pendant le combat !");
            return;
        }
        
        GameManager.MoveResult result = gameManager.movePlayer(direction);
        messageBar.setMessage(result.getMessage());
        
        // If combat started, handle enemy turn
        if (gameManager.getCombatManager().isCombatActive()) {
            messageBar.setMessage("Le combat commence ! Appuyez sur ESPACE pour attaquer.");
        }
    }
    
    /**
     * Player attacks in combat.
     */
    private void attack() {
        CombatManager.CombatResult heroResult = gameManager.playerAttack();
        messageBar.setMessage(heroResult.getMessage());
        
        // Check if hero attack failed (e.g., not enough PA)
        if (!heroResult.isSuccess()) {
            // Show popup for insufficient PA
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "Pas assez de Points d'Action pour attaquer !",
                "Action impossible",
                javax.swing.JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        // If combat still active, enemy attacks back
        if (gameManager.getCombatManager().isCombatActive()) {
            CombatManager.CombatResult enemyResult = gameManager.enemyTurn();
            messageBar.setMessage(heroResult.getMessage() + " | " + enemyResult.getMessage());
            
            // Check if hero died from enemy attack
            if (!enemyResult.isSuccess() || gameManager.isGameOver()) {
                // Hero died - call gameOver() to show proper popup
                gameOver();
            }
        } else if (heroResult.isSuccess()) {
            // Enemy defeated - show victory popup with XP gained
            javax.swing.JOptionPane.showMessageDialog(
                null,
                heroResult.getMessage() + "\n\n" +
                "Niveau actuel : " + gameManager.getHero().getLevel() + "\n" +
                "XP actuel : " + gameManager.getHero().getXP(),
                "Victoire !",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );
            messageBar.setMessage("Victoire ! Ennemi vaincu !");
        }
    }
    
    /**
     * Toggle inventory display.
     */
    private void toggleInventory() {
        showingInventory = !showingInventory;
        
        if (showingInventory) {
            inventoryUI.displayInventory(gameManager.getGameState());
            messageBar.setMessage("Inventaire ouvert. Appuyez sur I pour fermer.");
        } else {
            renderer.render(gameManager);
            sidePanel.update(gameManager);
            messageBar.setMessage("Inventaire fermé.");
        }
    }
    
    /**
     * Handle clicking on an item in inventory.
     */
    private void handleItemClick(Equipment item) {
        String action = "";
        
        // Show context menu
        String[] options;
        String defaultOption;
        if (item.isEquipped()) {
            options = new String[]{"Déséquiper", "Annuler"};
            defaultOption = "Déséquiper";
        } else if (item.getType() == Equipment.EquipmentType.POTION) {
            options = new String[]{"Utiliser", "Annuler"};
            defaultOption = "Utiliser";
        } else if (isRune(item.getType())) {
            options = new String[]{"Utiliser", "Annuler"};
            defaultOption = "Utiliser";
        } else {
            options = new String[]{"Equiper", "Annuler"};
            defaultOption = "Equiper";
        }
        
        // Show dialog and get user choice
        String choice = (String)javax.swing.JOptionPane.showInputDialog(
            null,
            "Que voulez-vous faire avec " + item.getName() + " ?",
            "Action sur l'objet",
            javax.swing.JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            defaultOption
        );
        
        // If user cancelled or closed dialog, do nothing
        if (choice == null || choice.equals("Annuler")) {
            return;
        }
        
        // Perform action based on user choice
        if (choice.equals("Déséquiper")) {
            gameManager.getInventory().unequipItem(item);
            action = "Déséquipé " + item.getName();
        } else if (choice.equals("Utiliser")) {
            boolean success = gameManager.getInventory().useItem(item);
            if (success) {
                action = "Utilisé " + item.getName();
            } else {
                action = "Impossible d'utiliser " + item.getName();
            }
        } else if (choice.equals("Equiper")) {
            gameManager.getInventory().equipItem(item);
            action = "Équipé " + item.getName();
        }
        
        // Refresh inventory display and side panel
        inventoryUI.displayInventory(gameManager.getGameState());
        sidePanel.update(gameManager);  // Update full side panel to reflect stat changes
        messageBar.setMessage(action);
    }
    
    /**
     * Check if item type is a rune.
     */
    private boolean isRune(Equipment.EquipmentType type) {
        return type.name().startsWith("RUNE_");
    }
    
    /**
     * Open dialog to spend stat points.
     */
    private void openStatPointDialog() {
        Hero hero = gameManager.getHero();
        
        // Check if there are stat points to spend
        if (hero.getUnspentStatPoints() <= 0) {
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "Vous n'avez pas de points de caractéristiques à dépenser.\n" +
                "Montez de niveau pour en gagner plus !",
                "Attribution de points",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
        // Show stat attribution dialog
        String[] stats = {"Force", "Agilité", "Sagesse"};
        String selectedStat = (String)javax.swing.JOptionPane.showInputDialog(
            null,
            "Vous avez " + hero.getUnspentStatPoints() + " points à dépenser.\n" +
            "Quelle caractéristique voulez-vous augmenter ?\n\n" +
            "Force actuelle: " + hero.getForce() + "\n" +
            "Agilité actuelle: " + hero.getAgility() + "\n" +
            "Sagesse actuelle: " + hero.getWisdom(),
            "Attribution de points de caractéristiques",
            javax.swing.JOptionPane.QUESTION_MESSAGE,
            null,
            stats,
            stats[0]
        );
        
        // If user cancelled, return
        if (selectedStat == null) {
            return;
        }
        
        // Ask how many points to spend
        String input = javax.swing.JOptionPane.showInputDialog(
            null,
            "Combien de points voulez-vous mettre en " + selectedStat + " ?\n" +
            "Points disponibles : " + hero.getUnspentStatPoints(),
            "Attribution de points",
            javax.swing.JOptionPane.QUESTION_MESSAGE
        );
        
        // If user cancelled, return
        if (input == null || input.trim().isEmpty()) {
            return;
        }
        
        // Parse amount
        int amount;
        try {
            amount = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "Nombre invalide ! Veuillez entrer un nombre entier.",
                "Erreur",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        // Validate amount
        if (amount <= 0) {
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "Le nombre de points doit être positif !",
                "Erreur",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        if (amount > hero.getUnspentStatPoints()) {
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "Vous n'avez pas assez de points disponibles !\n" +
                "Points disponibles : " + hero.getUnspentStatPoints(),
                "Erreur",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        // Spend the points
        String statKey = "";
        switch (selectedStat) {
            case "Force":
                statKey = "force";
                break;
            case "Agilité":
                statKey = "agility";
                break;
            case "Sagesse":
                statKey = "wisdom";
                break;
        }
        
        boolean success = hero.spendStatPoints(statKey, amount);
        
        if (success) {
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "+" + amount + " " + selectedStat + " !\n\n" +
                selectedStat + " : " + 
                (selectedStat.equals("Force") ? hero.getForce() : 
                 selectedStat.equals("Agilité") ? hero.getAgility() : 
                 hero.getWisdom()) + "\n" +
                "Points restants : " + hero.getUnspentStatPoints(),
                "Attribution réussie",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );
            
            // Update displays
            if (showingInventory) {
                inventoryUI.displayInventory(gameManager.getGameState());
            } else {
                sidePanel.update(gameManager);
            }
            messageBar.setMessage("+" + amount + " " + selectedStat + " !");
            
            // Ask if they want to spend more points
            if (hero.getUnspentStatPoints() > 0) {
                int choice = javax.swing.JOptionPane.showConfirmDialog(
                    null,
                    "Vous avez encore " + hero.getUnspentStatPoints() + " points à dépenser.\n" +
                    "Voulez-vous continuer ?",
                    "Attribution de points",
                    javax.swing.JOptionPane.YES_NO_OPTION
                );
                
                if (choice == javax.swing.JOptionPane.YES_OPTION) {
                    openStatPointDialog();
                }
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "Erreur lors de l'attribution des points.",
                "Erreur",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Save the game.
     */
    private void saveGame() {
        System.out.println("Attempting to save game to: " + Constants.DEFAULT_SAVE_FILE);
        boolean success = gameManager.saveGame(Constants.DEFAULT_SAVE_FILE);
        if (success) {
            messageBar.setMessage("Jeu sauvegardé avec succès !");
            System.out.println("Save successful!");
            
            // Show save success popup
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "Votre partie a été sauvegardée avec succès !\n" +
                "Fichier : " + Constants.DEFAULT_SAVE_FILE,
                "Sauvegarde réussie",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            messageBar.setMessage("Échec de la sauvegarde du jeu. Voir console pour détails.");
            System.err.println("Save failed! Check console for error details.");
            
            // Show save failure popup
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "Échec de la sauvegarde du jeu.\n" +
                "Vérifiez la console pour plus de détails.",
                "Erreur de sauvegarde",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Handle ESC key press - return to main menu with save confirmation.
     */
    private void handleEscapeKey() {
        // Show confirmation dialog with save option
        Object[] options = {"Sauvegarder et Quitter", "Quitter sans Sauvegarder", "Annuler"};
        int choice = javax.swing.JOptionPane.showOptionDialog(
            null,
            "Voulez-vous retourner au menu principal ?\n\n" +
            "⚠️ Assurez-vous de sauvegarder votre progression !",
            "Retour au Menu Principal",
            javax.swing.JOptionPane.YES_NO_CANCEL_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        if (choice == 0) { // Save and quit
            boolean saved = gameManager.saveGame(Constants.DEFAULT_SAVE_FILE);
            if (saved) {
                javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Partie sauvegardée avec succès !",
                    "Sauvegarde",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
                );
                returnToMainMenu();
            } else {
                // Show error but still ask if they want to quit
                int retry = javax.swing.JOptionPane.showConfirmDialog(
                    null,
                    "Échec de la sauvegarde !\n\n" +
                    "Voulez-vous quitter quand même ?",
                    "Erreur de Sauvegarde",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.ERROR_MESSAGE
                );
                if (retry == javax.swing.JOptionPane.YES_OPTION) {
                    returnToMainMenu();
                }
            }
        } else if (choice == 1) { // Quit without saving
            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                null,
                "Êtes-vous sûr de vouloir quitter sans sauvegarder ?\n\n" +
                "Toute progression non sauvegardée sera perdue !",
                "Confirmation",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE
            );
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                returnToMainMenu();
            }
        }
        // If choice == 2 (Cancel) or dialog closed, do nothing
    }
    
    /**
     * Confirm quit action.
     */
    private boolean confirmQuit() {
        System.out.println("Save before quitting? (Y/N)");
        // In a full implementation, show a dialog
        // For now, just save
        saveGame();
        return true;
    }
    
    /**
     * Handle game over.
     */
    private void gameOver() {
        System.out.println("\n=== GAME OVER ===");
        System.out.println("Votre héros est tombé !");
        System.out.println("Niveau final : " + gameManager.getHero().getLevel());
        System.out.println("Étage final : " + gameManager.getGameState().getCurrentFloor());
        
        // Show game over popup with "Try again" button
        int choice = javax.swing.JOptionPane.showOptionDialog(
            null,
            "Game Over!\n\n" +
            "Vous avez été vaincu !\n" +
            "Niveau final : " + gameManager.getHero().getLevel() + "\n" +
            "Étage final : " + gameManager.getGameState().getCurrentFloor() + "\n\n" +
            "Voulez-vous réessayer ?",
            "Game Over",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.ERROR_MESSAGE,
            null,
            new Object[]{"Réessayer", "Quitter"},
            "Réessayer"
        );
        
        returnToMainMenu();
    }
    
    /**
     * Return to main menu.
     */
    private void returnToMainMenu() {
        // Exit the application
        // In a full implementation, you could close the window and restart the menu
        // For now, just exit
        System.exit(0);
    }
    
    /**
     * Get French display name for enemy type.
     */
    private String getEnemyDisplayName(Mob.MobType type) {
        switch (type) {
            case SMALL:
                return "Petit Monstre";
            case LARGE:
                return "Grand Monstre";
            case BOSS:
                return "BOSS";
            default:
                return type.name();
        }
    }
    
    /**
     * Handle game won.
     */
    private void gameWon() {
        System.out.println("\n=== VICTOIRE ! ===");
        System.out.println("Félicitations ! Vous avez terminé le jeu !");
        System.out.println("Niveau final : " + gameManager.getHero().getLevel());
        System.out.println("XP final : " + gameManager.getHero().getXP());
        
        // Show victory popup
        javax.swing.JOptionPane.showMessageDialog(
            null,
            "🎉 Félicitations, vous avez terminé le jeu ! 🎉\n\n" +
            "Vous avez vaincu tous les boss et conquis tous les étages !\n\n" +
            "Statistiques finales :\n" +
            "├─ Niveau : " + gameManager.getHero().getLevel() + "\n" +
            "├─ XP : " + gameManager.getHero().getXP() + "\n" +
            "├─ Étages complétés : " + gameManager.getGameState().getCurrentFloor() + "\n" +
            "└─ Héros : " + gameManager.getGameState().getPlayerName() + "\n\n" +
            "Merci d'avoir joué !",
            "Victoire !",
            javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
        
        // Return to main menu
        returnToMainMenu();
    }
    
    /**
     * Main entry point.
     */
    public static void main(String[] args) {
        // Show menu and wait for game start
        GameMenu.showMenu(gameState -> {
            RPGGame game = new RPGGame(gameState);
            // Run the game on a separate thread to avoid blocking the EDT
            new Thread(() -> game.run()).start();
        });
    }
}
