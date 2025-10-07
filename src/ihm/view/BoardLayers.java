/**
 * 
 */
package ihm.view;

import ihm.model.Board;

import java.awt.Color;
import java.awt.Image;

import javax.swing.JLayeredPane;


/**
 * @author Julien Tesson
 *
 */
public class BoardLayers extends JLayeredPane {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	BoardBackground bg;

	BoardSprites bsprites;
	
	BoardSelector bselect;
	
	
	/**
	 * @param x
	 * @param y
	 * @param bgim
	 * @see ihm.view.BoardBackground#setBackground(int, int, java.awt.Image)
	 */
	public void setBackground(int x, int y, Image bgim, boolean repaint) {
		bg.setBackground(x, y, bgim, repaint);
	}

	/**
	 * @param x
	 * @param y
	 * @param c
	 * @see ihm.view.BoardSelector#select(int, int, java.awt.Color)
	 */
	public void select(int x, int y, Color c) {
		bselect.select(x, y, c);
	}
	
	/**
	 * @param b 
	 * 
	 */
	public BoardLayers(Board b) {
		bg = new BoardBackground(b);
		bsprites = new BoardSprites(b);
		bselect = new BoardSelector(b);
//		bg.setOpaque(false);
//		bsprites.setOpaque(false);
//		bselect.setOpaque(false);
		
//		bselect.setBackground(Color.BLUE);
//		bsprites.setBackground(Color.RED);

//		bsprites.setVisible(false);
//		bg.setVisible(false);
//		bselect.setVisible(false);

		this.add(bg,DEFAULT_LAYER);
		this.add(bsprites, JLayeredPane.PALETTE_LAYER);
		this.add(bselect, JLayeredPane.DRAG_LAYER);
//		setOpaque(true);
//		this.setBackground(Color.green);
		this.setPreferredSize(bg.getSize());
	}

	public void reinit() {
		bg.reinit();
		bsprites.reinit();
		bselect.reinit();
		this.setPreferredSize(bg.getSize());
	}

	public BoardBackground getBoardBackground() {
		return bg;
	}

	public BoardSelector getBoardSelector() {
		return bselect;
	}

	public BoardSprites getBoardSprites() {
		return bsprites;
	}

	public int tileWidth() {
		return BoardComponent.TILE_SIZE;
	}
	
	public int tileHeight() {
		return BoardComponent.TILE_SIZE;
	}
//	@Override
//	public void repaint() {
//		System.out.println("repainting layered component");
//		super.repaint();
//	}
	
}
