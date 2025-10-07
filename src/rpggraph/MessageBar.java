package rpggraph;

import ihm.FrontEnd;

/**
 * Manages the bottom message bar that displays game messages and last actions.
 */
public class MessageBar {
    
    private final FrontEnd frontEnd;
    private String currentMessage;
    
    public MessageBar(FrontEnd frontEnd) {
        this.frontEnd = frontEnd;
        this.currentMessage = "Bienvenue dans ce donjon !";
    }
    
    /**
     * Set and display a message in the bottom bar.
     */
    public void setMessage(String message) {
        if (message != null && !message.isEmpty()) {
            this.currentMessage = message;
            updateDisplay();
        }
    }
    
    /**
     * Get the current message.
     */
    public String getMessage() {
        return currentMessage;
    }
    
    /**
     * Update the message bar display.
     */
    private void updateDisplay() {
        frontEnd.setBottomBarMessage(currentMessage);
    }
    
    /**
     * Clear the message bar.
     */
    public void clear() {
        this.currentMessage = "";
        updateDisplay();
    }
}
