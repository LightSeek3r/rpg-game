package ihm.view.propriete;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ihm.model.Proprietes;

class SelectionListener implements ActionListener {
	int index;
	Proprietes p;
	String categorie;
	String propriete;
	public SelectionListener(Proprietes pr, String categorie, String propriete, int index) {
		this.index = index;
		this.p = pr;
		this.categorie = categorie;
		this.propriete = propriete;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		p.setChoixProp(categorie, propriete, Integer.parseInt(e.getActionCommand()));
	}
	
}