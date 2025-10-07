package ihm.view;

import ihm.model.Board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;


/**
 * Cette classe permet l'affichage des sprites, éléments graphiques déplaçables.
 * @author Julien Tesson
 *
 */
public class BoardSprites extends BoardComponent {

	private static final long serialVersionUID = -5770285641843589686L;
	Collection<Sprite> sprites;
//	TODO sprite overlap
	
	/**
	 * Build a Board to draw sprites, with no sprite and transparent background.
	 * @param b
	 */
	public BoardSprites(Board b) {
		super(b);
		offscreen.setBackground(new Color(0,0,0,0));
		sprites = new Vector<Sprite>() ;
//		if (GameFrame.logLevel > 2 )
//			System.out.println("offscreen :"+offscreen);
	}
	
	@Override
	public void reinit() {
		super.reinit();
		sprites = new ArrayList <Sprite>() ;
	}

	@Override
	public void paint(Graphics g) {
		paintSprites();		
		super.paint(g);
	}

	/**
	 * Update all sprites
	 */
	public void paintSprites() {
		synchronized (sprites) {
			for (Sprite sprite : sprites) {
				update(sprite,false);
			}		
		}
	}

	/**
	 * Add a sprite to the board
	 * @param S the sprite to be added
	 */
	public void addSprite(Sprite S) {
		synchronized (sprites) {
			sprites.add(S);
		}
	}
	

	/**
	 *  Update the sprite S
	 * @param sprite the sprite to be updated
	 * @param repaint if true, the board is repainted.
	 */
	void update(Sprite sprite, boolean repaint) {
		if (GameFrame.logLevel >= 2) {
			System.out.println("updating sprite on board");
			System.out.println("clear rectangle, visibility :"+ sprite.getVisibility());
		}
		offscreen.clearRect(sprite.getOldX() * TILE_SIZE, sprite.getOldY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
		if (sprite.getVisibility()) {	
			for (Image img : sprite) {
				if (GameFrame.logLevel >= 2) {
					System.out.println("drawingSprite "+ img +"at ("+ sprite.getX() +","+ sprite.getY() +")");
				}
				offscreen.drawImage(img, sprite.getX() * TILE_SIZE, sprite.getY() * TILE_SIZE, null);
			}
		}
		sprite.updated();
		if (repaint)
			BoardSprites.super.repaint();	
	}

	/**
	 * Remove the sprite given in parameter
	 * @param sprite The sprite to remove
	 */
	public void delete(Sprite sprite) {
		synchronized (sprites) {
			sprites.remove(sprite);
		}		
	}

}
