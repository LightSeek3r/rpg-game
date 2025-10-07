package base;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Menu system for the game.
 */
public class GameMenu extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private GameState gameState;
    private GameMenuListener listener;
    
    public GameMenu() {
        setTitle("RPG Game - Menu Principal");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    /**
     * Initialize menu components.
     */
    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Bienvenue dans le Donjon !");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // New Game button
        JButton newGameBtn = createMenuButton("Nouvelle Partie");
        newGameBtn.addActionListener(e -> showNewGameDialog());
        mainPanel.add(newGameBtn);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Load Game button
        JButton loadGameBtn = createMenuButton("Charger Partie");
        loadGameBtn.addActionListener(e -> loadGame());
        mainPanel.add(loadGameBtn);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Options button (future feature)
        JButton optionsBtn = createMenuButton("Options");
        optionsBtn.setEnabled(false); // Not implemented yet
        mainPanel.add(optionsBtn);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Quit button
        JButton quitBtn = createMenuButton("Quitter");
        quitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Êtes-vous sûr de vouloir quitter ?",
                "Quitter le jeu",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        mainPanel.add(quitBtn);
        
        add(mainPanel);
    }
    
    /**
     * Create a styled menu button.
     */
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        return button;
    }
    
    /**
     * Show new game dialog.
     */
    private void showNewGameDialog() {
        JDialog dialog = new JDialog(this, "Nouvelle Partie", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Player name
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nom du Joueur :"), gbc);
        
        gbc.gridx = 1;
        JTextField nameField = new JTextField(15);
        panel.add(nameField, gbc);
        
        // Hero class selection
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Choisissez la Classe :"), gbc);
        
        gbc.gridx = 1;
        String[] classes = {"Warrior", "Archer", "Mage"};
        JComboBox<String> classCombo = new JComboBox<>(classes);
        panel.add(classCombo, gbc);
        
        // Class description
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JTextArea descArea = new JTextArea(5, 30);
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setText(getClassDescription("Warrior"));
        
        classCombo.addActionListener(e -> {
            String selected = (String) classCombo.getSelectedItem();
            descArea.setText(getClassDescription(selected));
        });
        
        JScrollPane scrollPane = new JScrollPane(descArea);
        panel.add(scrollPane, gbc);
        
        // Buttons
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        
        JPanel buttonPanel = new JPanel();
        JButton startBtn = new JButton("Démarrer la Partie");
        JButton cancelBtn = new JButton("Annuler");

        startBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Veuillez entrer un nom de joueur !");
                return;
            }
            
            Hero.HeroType heroType = getHeroType((String) classCombo.getSelectedItem());
            createNewGame(heroType, name);
            dialog.dispose();
            this.dispose();
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(startBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Get class description for display.
     */
    private String getClassDescription(String className) {
        switch (className) {
            case "Warrior":
                return "Le Warrior excelle dans le combat rapproché avec une grande santé et force. " +
                       "Idéal pour les joueurs qui préfèrent le combat direct et encaisser les dégâts.";
            case "Archer":
                return "L'Archer se spécialise dans le combat à distance avec une grande agilité et portée. " +
                       "Idéal pour les joueurs qui préfèrent garder leurs distances et effectuer des attaques précises.";
            case "Mage":
                return "Le Mage manie une magie puissante avec une grande sagesse et des effets de zone. " +
                       "Idéal pour les joueurs qui préfèrent le lancement de sorts stratégiques et les attaques magiques.";
            default:
                return "";
        }
    }
    
    /**
     * Convert class name to HeroType.
     */
    private Hero.HeroType getHeroType(String className) {
        switch (className) {
            case "Warrior":
                return Hero.HeroType.WARRIOR;
            case "Archer":
                return Hero.HeroType.ARCHER;
            case "Mage":
                return Hero.HeroType.MAGE;
            default:
                return Hero.HeroType.WARRIOR;
        }
    }
    
    /**
     * Create a new game.
     */
    private void createNewGame(Hero.HeroType heroType, String playerName) {
        gameState = GameState.newGame(heroType, playerName);
        
        if (listener != null) {
            listener.onGameStart(gameState);
        }
    }
    
    /**
     * Load a saved game.
     */
    private void loadGame() {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setDialogTitle("Charger une Partie");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                return f.isDirectory() || f.getName().endsWith(".sav");
            }
            
            @Override
            public String getDescription() {
                return "Fichiers de Sauvegarde (*.sav)";
            }
        });
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                gameState = GameState.load(file.getAbsolutePath());
                
                JOptionPane.showMessageDialog(
                    this,
                    "Partie chargée avec succès !\nBienvenue de nouveau, " + gameState.getPlayerName(),
                    "Chargement réussi",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                if (listener != null) {
                    listener.onGameStart(gameState);
                }
                
                this.dispose();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Échec du chargement de la partie :\n" + ex.getMessage(),
                    "Échec du Chargement",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    /**
     * Set listener for menu events.
     */
    public void setMenuListener(GameMenuListener listener) {
        this.listener = listener;
    }
    
    /**
     * Interface for menu event callbacks.
     */
    public interface GameMenuListener {
        void onGameStart(GameState gameState);
    }
    
    /**
     * Show the main menu.
     */
    public static void showMenu(GameMenuListener listener) {
        SwingUtilities.invokeLater(() -> {
            GameMenu menu = new GameMenu();
            menu.setMenuListener(listener);
            menu.setVisible(true);
        });
    }
}
