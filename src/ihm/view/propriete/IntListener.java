package ihm.view.propriete;

import ihm.model.Proprietes;
import ihm.view.JIntField;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class IntListener implements CaretListener {
	Proprietes p;
	String categorie;
	String propriete;

	public IntListener(Proprietes p, String categorie, String name) {
		this.p = p;
		this.categorie = categorie;
		this.propriete = name;
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		p.setIntProp(categorie, propriete, ((JIntField)e.getSource()).getValue());
	}

}
