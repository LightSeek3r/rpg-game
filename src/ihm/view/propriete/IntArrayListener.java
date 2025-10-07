package ihm.view.propriete;

import ihm.model.Proprietes;
import ihm.view.JIntField;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class IntArrayListener implements CaretListener {
	Proprietes p;
	String categorie;
	String propriete;
	int pos;
	
	public IntArrayListener(Proprietes p, String categorie, String name, int i) {
		this.p = p;
		this.categorie = categorie;
		this.propriete = name;
		this.pos = i;
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		p.setIntArrayProp(categorie, propriete,pos, ((JIntField)e.getSource()).getValue());
	}

}
