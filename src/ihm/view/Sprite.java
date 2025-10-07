package ihm.view;

import ihm.FrontEnd;

import java.awt.Image;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Un Sprite est un dessin, de la taille d'une case, qui peut être déplacé sur le plateau.
 * Il peut être composé de plusieurs images superposée. 
 * La dernière image ajoutée est la plus haute.
 * La visibilité de chaque image peut être modifiée.  
 * @author Julien Tesson
 */
public class Sprite implements Iterable<Image> {
	
	private ArrayList<Image> images;
	private ArrayList<Boolean> visibleImages;
	private BoardSprites board;
	private int x = 0;
	private int y = 0;
	private int oldx = 0;
	private int oldy = 0;
	private boolean visible = true;
	private boolean update = false;

	/** 
	 * 	Construit un nouveau Sprite attaché à un plateau.
	 * 	Le sprite est invisible et n'a aucune image attachée.
	 * 	N'utilisez pas cette méthode ! utilisez {@link FrontEnd#creerSprite()}
	 * @param b Plateau auquel le Sprite est attaché.
	 */
	public Sprite(BoardSprites b) { 

		board = b;
		b.addSprite(this);
		images = new ArrayList<Image>();
		visibleImages = new ArrayList<Boolean>();
		visible = false;
		x = 0;
		y = 0;
		b.addSprite(this);
		update();
	}
	
	/** 
	 * Construit une copie d'un Sprite.
	 * Les images sont les mêmes que le sprite initial.
	 * @param s Sprite a copier.
	 */
	@SuppressWarnings("unchecked")
	public Sprite(Sprite s) {
		images = (ArrayList<Image>) s.images.clone();
		visibleImages = (ArrayList<Boolean>) s.visibleImages.clone();
		board = s.board;
		x = s.x;
		y = s.y;
		visible = false;
		board.addSprite(this);
		update();
	}
	
	
	/**
	 * Mise à jour de l'affichage de l'image.
	 */
	private void update() {
		if (GameFrame.logLevel >= 1)
			System.out.println("requesting sprite update");
		update = false;
		try {
			java.awt.EventQueue.invokeAndWait(
				new Runnable() {
					@Override
					public void run() {
						if (GameFrame.logLevel >= 1) {
							System.out.println("updating sprite");
						}
						board.update(Sprite.this, true);
					}
				});
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Methode de notification de la mise à jour.  
	 */
	void updated() {
		update = true;
		oldx = x;
		oldy = y;
//		System.out.println("sprite updated");
	}
	
	/**
	 * @return true si l'affichage du sprite est à jour.
	 */
	public boolean upToDate() {
		return update;
	}
		
	
	/**
	 * Ajoute une image au sprite (au dessus)
	 * L'image est visible 
	 * @param filename chemin vers le fichier d'image.
	 * @return l'index de l'image dans le Sprite
	 */
	public int addImage (String filename) {
		try {
			final Image img = ImageManager.getImage(filename);
			try {
				java.awt.EventQueue.invokeAndWait(
					new Runnable() {
						@Override
						public void run() {
							images.add(img);	
							visibleImages.add(Boolean.TRUE);	
							assert (images.size() == visibleImages.size()) : "Sprite : probleme de correlation entre les visibilités et les images";
						}
					});
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			update();
			return images.indexOf(img);
		}
		catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Rend l'image i visible
	 * @param i Index de l'image.
	 */
	public void showImage(final int i) {
		assert (i >= 0 && i < images.size());
		try {
			java.awt.EventQueue.invokeAndWait(
				new Runnable() {
					@Override
					public void run() {
						if (!visibleImages.get(i).booleanValue()) {
							visibleImages.set(i, Boolean.TRUE);
						}
					}
				});
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		this.update();
	}
	
	
	/**
	 * Rend l'image i invisible.
	 * @param i Index de l'image.
	 */	
	public void hideImage(final int i) {
		assert (i >= 0 && i < images.size());
		try {
			java.awt.EventQueue.invokeAndWait(
				new Runnable() {
					@Override
					public void run() {
						if (visibleImages.get(i).booleanValue()) {
							visibleImages.set(i, Boolean.FALSE);	
						}
					}
				});
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		this.update();
	}

	/**
	 * rend le Sprite visible si le parametre vaut true ou invisible si vaut false.
	 * @param visible visibilité du Sprite.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
		this.update();
	}

	/**
	 * Supprime le Sprite.
	 */
	public void delete() {
		board.delete(this);
	}

	/**
	 * @return la visibilité du Sprite
	 */
	public boolean getVisibility() {
		return visible;
	}
	
	/**
	 * Change la position du Sprite sur le plateau.
	 * @param x abscisse du Sprite.
	 * @param y ordonnée du Sprite.
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		update();
	}
	
	/**
	 * @return l'abscisse du Sprite.
	 */
	public int getX() {return x;}

	/**
	 * @return l'ordonnée du Sprite.
	 */
	public int getY() {return y;}
	

	/**
	 * @return l'abscisse du Sprite avant le dernier déplacement.
	 */
	int getOldX() {return oldx;}

	/**
	 * @return l'ordonnée du Sprite avant le dernier déplacement.
	 */
	int getOldY() {return oldy;}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		return new Sprite(this);
	}

	class imageIterator implements Iterator<Image> {
		boolean end = false;
		Image next = null;
		int index = 0;
		Iterator<Image> iit;
		Iterator<Boolean> vit;
		private boolean hasnext = false ;
		private boolean called = false;
		
		public imageIterator() {
			assert (images.size() == visibleImages.size()) : "Sprite : probleme de correlation entre les visibilités et les images, impossible d'itérer";
			iit = images.iterator();
			vit = visibleImages.iterator();				
		}
		
		@Override 
		public Image next() {
			called = false;
			return next;
		}
		
		@Override 
		public boolean hasNext() {		
			if (!called) {
			boolean trouve = false;			
			while (!trouve && !end) {
				if (vit.hasNext()) {
					index++;
					trouve = vit.next();
					next = iit.next();
				}
				else {
					next = null;
					end = true;
				}
			}
			called = true;
			hasnext = trouve;
			return trouve;
			}
			else { 
				return hasnext;
			}
		}
		
		@Override
		public void remove() {
			visibleImages.set(index, Boolean.FALSE);
			update();
		}
	}
	
	/**
	 * Iterateur permettant le parcours des images du Sprite
	 * Utilisé en interne pour l'affichage du sprite
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Image> iterator() {
				return new imageIterator();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Sprite [images="+ images +", visible="+ visible +", visibleImages="+ visibleImages +", x="+ x +", y="+ y +"]";
	}
}
