/**
 * 
 */
package ihm.controller;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Gestion des evenements d'interactions avec l'utilisateur <br/>
 * Un évènement peut correspondre à une touche préssée ou à un clic souris
 * @author Julien Tesson
 *
 */
public class Evenement {
	
	boolean mouse;
	MouseEvent mevent;
	
	KeyEvent kevent;
	
	int X;
	int Y;
	
	/**
	 * Construction d'un évènement souris
	 * @param me
	 * @param x
	 * @param y
	 */
	public Evenement(MouseEvent me, int x, int y) {
			mevent = me;
			X = x;
			Y = y;
			mouse = true;
	}
	
	/**
	 * Construction d'un évènement clavier
	 * @param ke
	 */
	public Evenement(KeyEvent ke) {
		kevent = ke;
		X = 0;
		Y = 0;
		mouse = false;
	}
	

	/**
	 * Dit si l'évènement est de type clic souris.
	 * @return true si il s'agit d'un clic de souris, false c'est une touche préssée. 
	 */
	public boolean isMouseEvent() {
		return mouse;
	}

	/**
	 * Dit si l'évènement est de type touche du clavier préssée.
	 * @return true si il s'agit d'une touche préssée, false si c'est un clic souris.
	 */
	public boolean isKeyboardEvent() {
		return (!mouse);
	}
	
	/**
	 * Donne l'abscisse de la case cliquée.
	 * @precondition l'evenement doit être un clic souris
	 * @return l'abscisse de la case cliquée.
	 */
	public int getXPosition() {
		assert (mouse) : "la position n'a de sens que pour les clics souris";
		return X;
	}
	
	/**
	 * Donne l'ordonnée de la case cliquée.
	 * @precondition l'evenement doit être un clic souris
	 * @return l'ordonnée de la case cliquée.
	 */
	public int getYPosition() {
		assert (mouse) : "la position n'a de sens que pour les clics souris";
		return Y;
	}
	
	/**
	 * Déscription de la touche préssée. 
	 * @return un code correspondant à la touche (les codes sont des constantes définis dans {@link KeyEvent})
	 */
	public int getKeyCode() {
		return kevent.getKeyCode();
	}
	
	/**
	 * Retourne le caractère correspondant à la touche préssée.
	 * @return le caractère de la touche
	 */
	public char getKey() {
		return kevent.getKeyChar();
	}
	
	/**
	 * Déscription du bouton préssé. 
	 * @return un code correspondant au bouton de la souris (les codes sont des constantes définis dans {@link MouseEvent})
	 */
	public int getMouseButton() {
		return mevent.getButton();
	}

	@Override
	public String toString() {
		if (isMouseEvent()) {
			return "mouse event, button :"+ getMouseButton() +"case "+ X +","+ Y;
		}
		else {
		return "keyboard event "+ kevent;
		}
	}

}