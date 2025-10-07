package ihm.model;

	
	
/**
 * @author Julien Tesson
 *
 */
public class Board {
	
	int largeur; 
	int hauteur; 
	
	/**
	 * Nouvelle Carte
	 * @param largeur
	 * @param hauteur 
	 */
	public Board(int largeur, int hauteur) {
		this.largeur = largeur;
		this.hauteur = hauteur;
	}

	/**
	 * @return largeur du plateau
	 */
	public int getWidth() {
		return largeur;
	}

	/**
	 * @return hauteur du plateau
	 */
	public int getHeight() {
		return hauteur;
	}

	/**
	 * @param largeur
	 * @param hauteur
	 */
	public void init(int largeur, int hauteur) {
		this.largeur = largeur;
		this.hauteur = hauteur;		
	}
}
	