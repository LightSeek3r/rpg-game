/**
 * 
 */
package ihm.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.Vector;

import ihm.model.Board;

/**
 * @author Julien Tesson
 *
 */
public class BoardSelector extends BoardComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8107775440196722468L;

	class Selection {
		int x;
		int y;
		Color c;
		
		Selection(int x, int y, Color c) {
			this.x = x;
			this.y = y;
			this.c = c;
		}
		@Override
		public String toString() {
			return "pos : "+ x +","+ y +" color"+ c;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((c == null) ? 0 : c.hashCode());
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Selection other = (Selection) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (c == null) {
				if (other.c != null)
					return false;
			} else if (!c.equals(other.c))
				return false;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}
		private BoardSelector getOuterType() {
			return BoardSelector.this;
		}
		
	}
	
	Vector<Selection> selected = new Vector<Selection> (); 
	
	/**
	 * @param b
	 */
	public BoardSelector(Board b) {
		super(b);
		offscreen.setComposite(AlphaComposite.Src);		
		selected = new Vector<Selection>();
	}

	public void select(int x, int y, Color c) {
		synchronized (selected) {
			selected.add(new Selection(x, y, c));			
		}
		repaint();	
	}
	
	public void deselect(int x, int y, Color c) {
		synchronized (selected) {
		selected.remove(new Selection(x, y, c));
		}
		unpaintSelection(x, y);
		paintSelections();
		try {
			java.awt.EventQueue.invokeAndWait(new Runnable() {
				@Override
				public void run() { repaint(); }
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void deselect(int x, int y){
		synchronized (selected) {
			Iterator<Selection> it = selected.iterator();
			while  (it.hasNext()){
				Selection select = it.next();
				if (select.x == x  && select.y == y) 
					it.remove();		
			}			
		}
		unpaintSelection(x, y);
		repaint();
	}

	public void unpaintSelection(int x, int y) {
		this.offscreen.setColor(new Color(0, 0, 0, 0));
		this.offscreen.drawRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
	}


	@Override
	public void reinit(){
		super.reinit();
		selected = new Vector<Selection>();
	}
	
	@Override
	public void paint(Graphics g) {
		paintSelections();
		super.paint(g);
	}
	
	
	private void paintSelections() {
		synchronized (selected) {
			for (Selection select : selected) {
				paintSelection(select);
			}
		}
	}
	

	private void paintSelection(Selection S) {
		this.offscreen.setColor(S.c);
		this.offscreen.drawRect(S.x * TILE_SIZE, S.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
		
	}	

}
