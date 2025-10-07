package ihm.view.propriete;

import java.util.Iterator;

import ihm.model.Proprietes;
import ihm.model.Proprietes.Choice;
import ihm.model.Proprietes.Prop;
import ihm.view.GameFrame;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class ProprietesVue extends JPanel {
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((p == null) ? 0 : p.hashCode());
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
		ProprietesVue other = (ProprietesVue) obj;
		if (p == null) {
			if (other.p != null)
				return false;
		}
		else if (!p.equals(other.p))
			return false;
		return true;
	}
	Proprietes p;
	private static final long serialVersionUID = -4757956067675481263L;

	
	protected static class IntVue extends JLabel {
		private static final long serialVersionUID = 8091093087529880878L;
		IntVue(String name, int i) {
			super(((name == null) ? "" : name +" = ")+ i);
		}
	}
	
	protected static class IntArrayVue extends JLabel {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5175143471061917981L;

		IntArrayVue(String name[], int i[]) {
			super(string(name, i));
		}
	
		static private String string(String name[], int i[]) {
			assert (i != null) : "impossible d'afficher un tableau inexistant";
			String chaine = "<html>";
			if (name == null) {
				for (int j = 0; j < i.length; j++) {
					chaine = chaine + i[j] +"<br/>";
				}
			}
			else {
				int j = 0;
				for (j = 0; j < name.length; j++) {
					chaine = chaine + name +" "+ i +"<br/>";
				}
				for (int j2 = j; j2 < i.length; j2++) {
					chaine = chaine + i[j] +"<br/>";
				}
			}
			return chaine + "</html>";
		}
	}
	
	protected static class choixVue extends JLabel {
		Choice choix;
		/**
		 * 
		 */
		private static final long serialVersionUID = 1142539129146639889L;
		
		public choixVue(Choice c) {
			super(c.s[c.selected]);
			choix = c;
		}
		
		public void update(){
			this.setText(choix.s[choix.selected]);
		}
	}
	
	protected static class StringVue extends JLabel {
		private static final long serialVersionUID = 1958389097217770983L;

		public StringVue(String s) {
			super(s);
		}
	}
	
	protected static class StringArrayVue extends JLabel {
		private static final long serialVersionUID = 6690967054517095840L;
		public StringArrayVue(String[] s) {
			super(concatenation(s));
		}
		public static String concatenation(String [] s) {
			String chaine = "<html>";
			if (s == null)
				return chaine;
			for (int i = 0; i < s.length; i++) {
				chaine += s[i] +"<br/>";
			}
			return 	chaine +"</html>";					
		}
	}
	
	public static class CategoryVue extends JPanel implements UpdateListener {
		private static final long serialVersionUID = -7018675461035618680L;
		Box boite;
		Proprietes p;
		String categorie;
		
		public CategoryVue(Proprietes p, String categorie) {
			super();
			this.p = p;
			this.categorie = categorie;
			String description = p.getDescriptionCategorie(categorie);
			boite = Box.createVerticalBox();
			boite.setBorder(BorderFactory.createTitledBorder(categorie + (description == null || description == "" ? "" : " : " + description)));
			for (Iterator<Prop> props = p.iterProperties(categorie); props.hasNext();) {
				Proprietes.Prop prop = props.next();
				boite.add(createIface(categorie, prop, p));
			}
			this.add(boite);
			p.addListener(this, categorie);
		}
		
		private Box createIface(String categorie, Prop prop, Proprietes p) {
				Box proppanel = Box.createVerticalBox();
				proppanel.setBorder(BorderFactory.createEmptyBorder(3, 1, 3, 1));
				String description = p.getDescriptionPropriete(categorie, prop.name, prop.type);
				if (description != null && description != "")
					proppanel.add(new JLabel("--- "+ description + " ---"), SwingConstants.CENTER);
				switch (prop.type) {          
				case Proprietes.CHOICE_PROP:
					proppanel.add(new choixVue(p.getChoix(categorie, prop.name)));
					break;
				case Proprietes.INTEGER_ARRAY_PROP:
					proppanel.add(new IntArrayVue(null, p.getIntArrayProp(categorie, prop.name)));
					break;
				case Proprietes.INTEGER_PROP:
					proppanel.add(new IntVue(prop.name,p.getIntProp(categorie, prop.name)));
					break;
				case Proprietes.STRING_ARRAY_PROP:
					proppanel.add(new StringArrayVue(p.getTableauStringProp(categorie, prop.name)));
					break;
				case Proprietes.STRING_PROP:
					proppanel.add(new StringVue(p.getStringProp(categorie, prop.name)));
					break;
				default:
					assert false : "type de propriete inconnu";
					break;				
				}
				return proppanel;
			
		}

		@Override
		public void update() {
			java.awt.EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					CategoryVue.this.remove(boite);
					boite = Box.createVerticalBox();
					boite.setBorder(BorderFactory.createTitledBorder(categorie));
					for (Iterator<Prop> props = p.iterProperties(categorie); props.hasNext();) {
						Proprietes.Prop prop = props.next();
						boite.add(createIface(categorie, prop, p));
					}
					CategoryVue.this.add(boite);
					CategoryVue.this.setVisible(false);
					CategoryVue.this.setVisible(true);
				}
			});
		}

		@Override
		public void updated(String propriete, int PROP_TYPE) {
			if (GameFrame.logLevel >= 1){
				System.err.println(propriete +" PropVue updated "+ PROP_TYPE);
			}
			update();
		}
	}
	
	public ProprietesVue (Proprietes p) {
		super();
		Box boite = Box.createVerticalBox();
		boite.setBorder(BorderFactory.createLoweredBevelBorder());
		for (Iterator<String> iterator = p.iterCategories(); iterator.hasNext();) {
			String categorie = iterator.next();
			boite.add(new CategoryVue(p, categorie));
			System.out.println("Nouvelle boite : "+ categorie);
		}
		this.add(boite);
		this.setMinimumSize(boite.getSize());
	}
}
