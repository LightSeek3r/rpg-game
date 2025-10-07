package ihm.view.propriete;

import ihm.model.Proprietes;

import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * @author Julien Tesson
 *
 */
public class TexteTableauListener implements CaretListener {
	Proprietes p;
	String categorie;
	String propriete;
	int pos;

	/**
	 * @param p
	 * @param categorie
	 * @param name
	 * @param pos
	 */
	public TexteTableauListener(Proprietes p, String categorie, String name,int pos) {
		this.p = p;
		this.categorie = categorie;
		this.propriete = name;
		this.pos = pos;
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		p.setTableauStringProp(categorie, propriete, pos, ((JTextField)e.getSource()).getText());
	}

}
