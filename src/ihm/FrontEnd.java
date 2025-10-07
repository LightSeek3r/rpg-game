package ihm;
import java.awt.Color;
import java.awt.Image;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;

import ihm.controller.*;
import ihm.model.Proprietes;
import ihm.view.*;
import ihm.view.propriete.ProprietesDialog;


/**
 * <h1>Front End pour attaquer l'interface graphique.</h1>
 * <br>Cette classe permet d'interagir avec l'interface graphique dessinant un plateau.</br>
 * @author Julien Tesson
 */
public class FrontEnd {

	private GameFrame GF; 
		
	/**
	 * Génération d'une interface graphique contenant un plateau de jeu.
	 * @param largeur Largeur du plateau (abscisse)
	 * @param hauteur Hauteur du plateau (ordonée)
	 */
	public FrontEnd(final int largeur, final int hauteur) {
		try {
			// Check if we're already on the EDT
			if (java.awt.EventQueue.isDispatchThread()) {
				// Already on EDT, just create directly
				GF = new GameFrame(largeur, hauteur);
				GF.setVisible(true);
			} else {
				// Not on EDT, use invokeAndWait
				java.awt.EventQueue.invokeAndWait(new Runnable() {
					 public void run() {
				        	GF = new GameFrame(largeur, hauteur);
				        	GF.setVisible(true);
					 }
				 });
			}
		}
		catch (Exception e) {
			exceptionInattendue(e);
		}
	}

	/**
	 * Génération d'une interface graphique contenant un plateau de jeu avec la même image de fond pour chaque case.
	 * @param largeur Largeur du plateau (abscisse)
	 * @param hauteur Hauteur du plateau (ordonée)
	 * @param defaultBackgroundImage image de fond
	 */
	public FrontEnd(final int largeur, final int hauteur, String defaultBackgroundImage) {
		Image img;
		try {
			img = ImageManager.getImage(defaultBackgroundImage);
		}
		catch (FileNotFoundException e1) {
			throw new RuntimeException(e1);
		}
		try {	
			// Check if we're already on the EDT
			if (java.awt.EventQueue.isDispatchThread()) {
				// Already on EDT, just create directly
				GF = new GameFrame(largeur, hauteur);
				GF.setVisible(true);
			} else {
				// Not on EDT, use invokeAndWait
				java.awt.EventQueue.invokeAndWait(new Runnable() {
					public void run() {
						GF = new GameFrame(largeur, hauteur);
						GF.setVisible(true);
					}
				});
			}

		}
		catch (Exception e) {
			exceptionInattendue(e);
		}
		BoardBackground bg = GF.getBoardBackground();
		for (int i = 0; i < largeur; i++){
			for (int j = 0; j < hauteur; j++) {
				bg.setBackground(i, j, img, false);
			}
		}
		bg.update();
		
	}

	
	/**
	 * Méthode interne pour arrêter le programme lors d'exception inattendue. 
	 * @param e Exception non attendue
	 */
	private void exceptionInattendue(Exception e) {
		System.err.println("exception inattendu, signalez-le svp");
		e.printStackTrace();			
	}

	// gestion de la carte
	/**
	 * Chargement d'une image de fond pour une case.
	 * @param x Abscisse de la case.
	 * @param y Ordonnée de la case.
	 * @param backgroundImage chemin vers l'image à afficher en fond.
	 */
	public void setBackground(final int x, final int y, String backgroundImage) {
		setBackground(x, y, backgroundImage, false);  // Don't repaint by default
	}
	
	/**
	 * Chargement d'une image de fond pour une case.
	 * @param x Abscisse de la case.
	 * @param y Ordonnée de la case.
	 * @param backgroundImage chemin vers l'image à afficher en fond.
	 * @param repaint if true, triggers immediate repaint
	 */
	public void setBackground(final int x, final int y, String backgroundImage, final boolean repaint) {
		try {
			final Image img = ImageManager.getImage(backgroundImage);
			try {	
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						GF.getBoardBackground().setBackground(x, y, img, repaint);	
					}});
			}
			catch (Exception e) {
				exceptionInattendue(e);
			}					
		}
		catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * Selection d'une zone.
	 * Cette méthode encadre la case par la couleur donnée.
	 * @param x abcisse de la case à séléctionner
	 * @param y ordonnée de la case à séléctionner
	 * @param c couleur de sélection (see {@link Color})
	 */
	public void selectionner(int x, int y, Color c) {
		GF.getBoardSelector().select(x, y, c);			
	}
	
	/**
	 * Désélection d'une zone, pour une couleur donnée.
	 * Si la zone à été précédement séléctionnée en utilisant la couleur donnée, cette couleur disparait.
	 * <em>Attention, si la zone à été sélectionnée plusieurs fois avec une même couleur, il faudra appeler
	 * désélection autant de fois pour que la couleur disparaisse. 
	 * </em> 
	 * @param x abscisse de la case à déséléctionner
	 * @param y ordonnée de la case à déséléctionner
	 * @param c couleur de sélection à enlever(see {@link Color})
	 */
	public void deselectionner(int x, int y, Color c) {
		GF.getBoardSelector().deselect(x, y, c);
	}

	/**
	 * Désélection d'une zone.
	 * Toutes les sélections de la case disparaissent.
	 * @param x abscisse de la case à déséléctionner
	 * @param y ordonnée de la case à déséléctionner
	 */
	public void deselectionner(int x, int y) {
		GF.getBoardSelector().deselect(x, y);
	}
	
//		sprite gestion
	/**
	 * Création d'un Sprite.
	 * Le sprite n'est pas visible par défaut.
	 * see {@link Sprite}
	 * @return Un sprite attaché à l'interface graphique.
	 */
	public Sprite creerSprite() {
		return new Sprite(GF.getBoardSprites());
	}

	/**
	 * Création d'un Sprite. 
	 * Le sprite sera initialisé au coordonnée donnée, avec l'image donnée.
	 * Le sprite est visible par défaut.
	 * @param x L'abscisse initiale du Sprite. 
	 * @param y L'ordonnée initiale du Sprite.
	 * @param imageFile la première image du Sprite.
	 * @return Un sprite attaché à l'interface graphique.
	 */
	public Sprite creerSprite(int x, int y, String imageFile) {
		Sprite s = creerSprite();
		s.setPosition(x, y);
		s.addImage(imageFile);
		s.setVisible(true);
		return s;
	}
	
	/**
	 * Création d'une copie d'un Sprite.
	 * Après duplication, il sera indépendant du sprite cloné.
	 * Le sprite n'est pas visible par défaut.
	 * @param s Le Sprite à dupliquer.
	 * @return Un sprite identique attaché à l'interface graphique.
	 */
	public Sprite copierSprite(Sprite s) {
		return new Sprite(s);
	}


	/**
	 * Lecture des intéraction de l'utilisateur avec le plateau.
	 * Cette méthode est bloquante : elle attend que l'utilisateur n'inter-agisse avec le plateau.  
	 * @return un {@link Evenement} provoqué par l'utilisateur. 
	 */
	public Evenement lireEvenement() {
		return GF.readEvent();
	};
	
	/**
	 * Lecture des intéraction de l'utilisateur avec le plateau.
	 * Cette méthode se comporte comme la méthode de même nom sans paramètre, sauf que si l'utilisateur n'as toujours pas inter-agis avec le plateau au bout du temps donné, elle retourne null.  
	 * @param timeout Temps d'attente.
	 * @return un {@link Evenement} provoqué par l'utilisateur, null si il n'a pas agis avant le timeout.
	 */
	public Evenement lireEvenement(long timeout) {
		return GF.readEvent(timeout);
	};
	
	
	/**
	 * Demande un entier à l'utilisateur
	 * @param question : Question posée à l'utilisateur
	 * @return l'entier entré par l'utilisateur
	 */
	public int lireInt(final String question) {
		return GF.readInt(" ", question);
	}
	
	
	/**
	 * Ouvre une boite de dialogue permettant de mettre à jour toutes les propriétés
	 * @param p Propriétés à mettre à jour (attention la propriété sera modifiée).
	 * @return Propriétés mises à jour.
	 */
	public Proprietes read(Proprietes p) {
		final ProprietesDialog paraBox = new ProprietesDialog(GF, true, p);
		try {
			java.awt.EventQueue.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					paraBox.setVisible(true);
				}
			});
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		paraBox.waitClose();
		return p;
	}
	
	/**
	 * Afficher les paramètres d'une Propriété
	 * @param p Propriétés à afficher.
	 * @param visible Visibilité de la propiété
	 */
	public void affiche(Proprietes p, boolean visible) {
		GF.print(p, visible);
	}
	
	/**
	 * Clear all side panel properties.
	 */
	public void clearSidePanel() {
		GF.clearPrintPanel();
	}
	
	/**
	 * Finalize side panel updates and refresh the layout.
	 */
	public void finalizeSidePanel() {
		GF.packFrame();
	}
	
	/**
	 * Set the message to display in the bottom bar.
	 * @param message The message to display.
	 */
	public void setBottomBarMessage(String message) {
		GF.setBottomMessage(message);
	}
	
	
	/**
	 * mise en attente 
	 * @param millisecondes durée de l'attente.
	 */
	public void attente(long millisecondes) {
		synchronized (this) {
			try {
				this.wait(millisecondes);
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Force the display to update/repaint.
	 */
	public void updateDisplay() {
		try {
			java.awt.EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					GF.getBoardBackground().repaint();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//		public void repaint(){
//			GF.repaint();
//		}

	public void setLog(int i) {
		GF.setLog(i);
	}

}
