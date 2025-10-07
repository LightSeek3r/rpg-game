package ihm.view.propriete;

import ihm.model.Proprietes;

import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * @author Julien Tesson
 *
 */
public class TexteListener implements CaretListener {
	Proprietes p;
	String categorie;
	String propriete;

	/**
	 * @param p
	 * @param categorie
	 * @param name
	 */
	public TexteListener(Proprietes p, String categorie, String name) {
		this.p = p;
		this.categorie = categorie;
		this.propriete = name;
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		p.setStringProp(categorie, propriete, ((JTextField)e.getSource()).getText());
	}

}
