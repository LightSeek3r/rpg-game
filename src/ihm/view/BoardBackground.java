package ihm.view;

import ihm.model.Board;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Graphics;

/**
 * @author Julien Tesson
 *
 */
public class BoardBackground extends BoardComponent {
	
//	BufferedImage offscreenImg;
//	Graphics2D offscreen;
	private Image[][] backgrounds;

	
	/**
	 *Serial ID 
	 */
	private static final long serialVersionUID = 5128119895742923850L;
	
	/**
	 * @param b
	 */
	public BoardBackground(Board b) {
		super(b);
		backgrounds = new Image[b.getWidth()][b.getHeight()];
		this.setMinimumSize(new Dimension(b.getWidth() * TILE_SIZE, b.getHeight() * TILE_SIZE));
	}

	@Override
	public void reinit() {
		super.reinit();
		backgrounds = new Image[b.getWidth()][b.getHeight()];
	}
	
	@Override
	public void paint(Graphics g) {
		paintBackground();
		super.paint(g);
	}
	/**
	 * @param x
	 * @param y
	 * @param bgim
	 * @param repaint
	 */
	public void setBackground(int x, int y, Image bgim, boolean repaint) {
		backgrounds[x][y] = bgim;
		paintBackground(x, y);
		if (repaint)
			repaint();  // Use repaint() directly instead of update()
	}
	
	private void paintBackground() {
		for(int i = 0; i < b.getWidth(); i++)
			for(int j = 0; j < b.getHeight(); j++)
				paintBackground(i, j);
	}

	public void paintBackground(int x, int y) {//.getScaledInstance(TILE_SIZE, TILE_SIZE, java.awt.Image.SCALE_DEFAULT)
		if(backgrounds[x][y] != null)
			offscreen.drawImage(backgrounds[x][y], x * TILE_SIZE, y * TILE_SIZE, this);
	}
	
	public void update() {
		try {
			java.awt.EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					BoardBackground.this.repaint();					
				}
			});
		}
		catch (Exception e) {
			e.printStackTrace();
		}
}
	
}
