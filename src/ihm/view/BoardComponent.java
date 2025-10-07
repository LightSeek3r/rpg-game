package ihm.view;

//import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import ihm.model.Board;

import javax.swing.JComponent;

public class BoardComponent extends JComponent{
	
	
	/**
	 *Serial ID 
	 */
	private static final long serialVersionUID = 5128119895742927850L;

	public static final int TILE_SIZE = 24;
	
	Board b;
	
	/**
	 * Pour le double buffering
	 */
	BufferedImage offscreenImg;
	/**
	 * Pour le double buffering
	 */
	Graphics2D offscreen;
	/**
	 * Pour éviter de repeindre le background à chaque fois
	 */
	

	public BoardComponent(Board b) {
		assert (b != null);
		this.b = b;
		offscreenImg = new BufferedImage(b.getWidth() * TILE_SIZE, b.getHeight() * TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
		offscreen = offscreenImg.createGraphics();
//		offscreen.setComposite(AlphaComposite.Src);

		offscreen.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		offscreen.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		offscreen.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		offscreen.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);

		this.setSize(b.getWidth() * TILE_SIZE, b.getHeight() * TILE_SIZE);
		this.setPreferredSize(getSize());	
	}
	
	
	public void reinit(){
		this.setSize(b.getWidth() * TILE_SIZE, b.getHeight() * TILE_SIZE);
		this.setPreferredSize(getSize());
		
		offscreenImg = new BufferedImage(b.getWidth() * TILE_SIZE, b.getHeight() * TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
		offscreen = offscreenImg.createGraphics();
		//offscreen.setComposite(AlphaComposite.Src);
		
		offscreen.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		offscreen.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		offscreen.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		offscreen.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);

		this.setSize(b.getWidth() * TILE_SIZE, b.getHeight() * TILE_SIZE);
		this.setPreferredSize(getSize());
	}
	
	@Override
	public void paint(Graphics g) {
//		System.out.println("painting component"+this);
		g.drawImage(offscreenImg, 0, 0, this);
	}
	
	@Override
	public void repaint() {
//		System.out.println("REpainting component"+this);
		super.repaint();
	}

	

}
