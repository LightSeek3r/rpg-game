/**
 * 
 */
package ihm.controller;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import ihm.model.Board;
import ihm.view.BoardLayers;


/**
 * @author Julien Tesson
 *
 */
public class Controller  implements KeyListener, MouseListener {

	BoardLayers view;
	Board model;
	List<Evenement> events;
	
	/**
	 * @param model
	 * @param view
	 */
	public Controller(Board model, BoardLayers view) {
		this.view = view;
		this.model = model;
		events = new ArrayList<Evenement>(8);
	}


	public void keyPressed(KeyEvent e) {
		synchronized (events) {
			events.add(new Evenement(e));
			events.notify();
		}
	}

	public void keyReleased(KeyEvent e) {
		// nothing
	}

	public void keyTyped(KeyEvent e) {
		// nothing
	}


	/**
	 * @return
	 */
	public Evenement getNextEvent() {
		return getNextEvent(0); 
	}

	/**
	 * @param timeout
	 * @return
	 */
	public Evenement getNextEvent(long timeout) {
		Evenement e = null;
		synchronized (events) {
			if (events.isEmpty()) {
				try {
					events.wait(timeout);
				}
				catch (InterruptedException e1) {};
			}
			if (!events.isEmpty()) {
				e = events.get(0);
				events.remove(0);
			}
		}
		return e;
	}


	
	public void mouseClicked(MouseEvent e) {
		Point position = e.getPoint();
		int x = position.x / view.tileWidth();
		if (x >= model.getWidth())
			x = model.getWidth() - 1;
		int y = position.y / view.tileHeight();
		if (y >= model.getHeight())
			y = model.getHeight() - 1;
		synchronized (events) {
			events.add(new Evenement(e, x, y));
			events.notify();
		}
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// nothing		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// nothing		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// nothing
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// nothing
	}

}
