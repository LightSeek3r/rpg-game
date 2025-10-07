package ihm.view.propriete;

import ihm.model.Proprietes;
import ihm.model.Proprietes.Prop;
import ihm.view.JIntField;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * @author Julien Tesson
 *
 */
public class ProprietesDialog extends JFrame implements ActionListener {

	JPanel PropertiesPanel;
	
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 2593720710009995770L;

	/**
	 * @param owner
	 * @param modal
	 * @param p
	 */
	public ProprietesDialog(JFrame owner, boolean modal, Proprietes p) {
		super();
		PropertiesPanel = new JPanel(new FlowLayout());
		setResizable(true); 
//		initThemes();
		for (Iterator<String> iterator = p.iterCategories(); iterator.hasNext();) {
			String categorie = iterator.next();
			this.add(categorie, p);				
		}
		
		JButton finish = new JButton("Terminer");
		finish.addActionListener(this);
		getContentPane().add(PropertiesPanel, BorderLayout.CENTER);
		getContentPane().add(finish, BorderLayout.SOUTH);
		this.pack();
	}	
	/**
	 * @param categorie
	 * @param p
	 */
	public void add(String categorie, Proprietes p) {
		Box catpanel = Box.createVerticalBox();
		catpanel.setBorder(BorderFactory.createTitledBorder(categorie));
		JLabel text =  new JLabel(p.getDescriptionCategorie(categorie));
		catpanel.add(text);
		for (Iterator<Prop> props = p.iterProperties(categorie); props.hasNext();) {
			Proprietes.Prop prop = props.next();
			catpanel.add(createIface(categorie, prop, p));
		}
//		System.out.println(categorie+" added to panel");
		PropertiesPanel.add(catpanel);
	}
	
	
	private Box createIface(String categorie, Prop prop, Proprietes p) {
		Box proppanel = Box.createVerticalBox();
		proppanel.setBorder(BorderFactory.createEmptyBorder(3, 1, 3, 1));
		String descr = p.getDescriptionPropriete(categorie, prop.name, prop.type);
		proppanel.add(new JLabel("-------------"+ (descr == null ? "" : descr) +"-------------"), SwingConstants.CENTER);
		switch (prop.type) {      
		case Proprietes.CHOICE_PROP:
			ButtonGroup group = new ButtonGroup();				
			Proprietes.Choice choix = p.getChoix(categorie, prop.name);
			JRadioButton[] buttons = new JRadioButton[choix.s.length];
			for (int i = 0; i < buttons.length; i++) {
				buttons[i] = new JRadioButton(choix.s[i], i == choix.selected);
				buttons[i].setActionCommand(Integer.toString(i));
				buttons[i].addActionListener(new SelectionListener(p, categorie, prop.name, i));
				group.add(buttons[i]);
				proppanel.add(buttons[i]);
			}
			break;
		case Proprietes.INTEGER_ARRAY_PROP:
			int[] entiers = p.getIntArrayProp(categorie, prop.name);
			JIntField[] intFields = new JIntField[entiers.length];
			for (int i = 0; i < intFields.length; i++) {
				intFields[i] = new JIntField(entiers[i], 6);
				intFields[i].addCaretListener(new IntArrayListener(p, categorie,prop.name,i));
				proppanel.add(intFields[i]);
			}
			break;
		case Proprietes.INTEGER_PROP:
			int entier = p.getIntProp(categorie, prop.name);
			JIntField intField = new JIntField(entier,6);
			intField.addCaretListener(new IntListener(p, categorie, prop.name));
			proppanel.add(intField);
			break;
		case Proprietes.STRING_ARRAY_PROP:
			String[] chaines = p.getTableauStringProp(categorie, prop.name);
			JTextField[] TextFields = new JTextField[chaines.length];
			for (int i = 0; i < TextFields.length; i++) {
				TextFields[i] = new JTextField(chaines[i], 35);
				TextFields[i].addCaretListener(new TexteTableauListener(p, categorie, prop.name, i));
				proppanel.add(TextFields[i]);
			}
			break;
		case Proprietes.STRING_PROP:
			String chaine = p.getStringProp(categorie, prop.name);
			JTextField TextField = new JTextField(chaine, 35);
			TextField.addCaretListener(new TexteListener(p, categorie, prop.name));
			proppanel.add(TextField);
			break;
		default:
			assert false : "type de propriété inconnu";
			break;				
		}
		return proppanel;
	}




	public void actionPerformed(ActionEvent e) {
		synchronized (this) {
			this.notify();
		}
		this.dispose();		
	}
	
	/**
	 * Mise en attente de la cloture de la fenetre.
	 */
	public void waitClose() {
		synchronized (this) {
			try {
				this.wait();
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
