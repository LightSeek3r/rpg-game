/**
 * 
 */
package ihm.model;

import ihm.view.GameFrame;
import ihm.view.propriete.UpdateListener;
import ihm.view.propriete.ProprietesVue.CategoryVue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Classe regroupant un ensemble de propriétés, accessible via leur nom.
 * @author Julien Tesson
 */
public class Proprietes {
	
	
	/**
	 * Constante designant une propriété de type entier
	 */
	public final static int INTEGER_PROP = 0;
	
	/**
	 * Constante designant une propriété de type tableau d'entiers
	 */
	public final static int INTEGER_ARRAY_PROP = 1;
	
	/**
	 * Constante designant une propriété de type chaine de caractères
	 */
	public final static int STRING_PROP = 2;
	
	/**
	 * Constante designant une propriété de type tableau de chaines de caractères
	 */
	public final static int STRING_ARRAY_PROP = 3;
	
	/**
	 * Constante designant une propriété de type Choix
	 */
	public final static int CHOICE_PROP = 4;
	
	Map<String,Category> categories;
	
	/**
	 * construit une nouvelle base de propriétés.
	 */
	public Proprietes() {
		categories = new HashMap<String,Category>();
//		new Category("default");
	}
	
	
	/**
	 * @author julien
	 *
	 */
	public class Choice {
		/**
		 * Les choix
		 */
		public String[] s;
		public int selected;
		Choice (String[] s, int selected) {
				this.s = s;
				this.selected = selected;
		}
		
		/**
		 * @param sel
		 */
		public void select(int sel) {
			selected = sel;
		}
		
		String selected() {
			return new String(s[selected]);
		}
		
		@Override
		public String toString() {
			return "Choice [s="+ Arrays.toString(s) +", selected="+ selected +"]";
		}
	}

	
	/**
	 *	ne pas utiliser
	 */
	public class Prop {
		/**
		 * Nom de la propriété
		 */
		public String name;
		
		/**
		 * Type de la propriété
		 */
		public int type;
		Prop (String Name, int type) {
			this.name = Name;
			this.type = type;
		}
		
		@Override
		public boolean equals(Object obj) {
//			System.out.print("prop equals ?");
			Prop prop;
			if (!obj.getClass().equals(this.getClass())) {
//				System.out.println("no : not the same class");
				return false;
			}
			else {
				prop = (Prop)obj;
//				System.out.println(this +" equals"+prop +"?"+(this.name.equals(prop.name) && this.type == prop.type));
				return (this.name.equals(prop.name) && this.type == prop.type);
			}
		}
		
		@Override
		public String toString() {
			return "{"+ name +";"+ type +"}";
		}
		
		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return name.hashCode()+ type;
		}
	}

	
	/**
	 * ne pas utiliser
	 */
	public class Category {
		 String name;
		 String description;
		 ArrayList<Prop> props;
		 ArrayList<UpdateListener> views;
		 Map<Prop,String> descriptions;
		 Map<String,String[]> strArray;
		 Map<String,String> str;
		 Map<String,int[]> intArray;
		 Map<String,Integer> entiers;
		 Map<String,Choice> choices;
		 
		 /**
		 * @param name
		 */
		public Category(String name) {
			 this.name = name;
			 props = new ArrayList<Prop>();
			 views = new ArrayList<UpdateListener>();
			 descriptions = new HashMap<Prop,String>();
			 strArray = new HashMap<String,String[]>();
			 str = new HashMap<String,String>();
			 intArray = new HashMap<String,int[]>();
			 entiers = new HashMap<String,Integer>();
			 choices = new HashMap<String,Choice>();
			 Proprietes.this.categories.put(name, this);
			 
		}

		void add(String nomPropriete, int propType) {
				props.add(new Prop(nomPropriete, propType));
		}

		/**
		 * 
		 */
		public void changed() {
			for (UpdateListener ul : views) {
				ul.update();
			}
		}
		
		/**
		 * @param ul
		 */
		public void add(UpdateListener ul) {
			views.add(ul);
		}

		@Override
		public String toString() {
			return "Category [choices="+ choices +", description="+ description +", descriptions="+ descriptions +", entiers="+ entiers +", intArray="+ intArray +", name="+ name +", props="+ props +", str="+ str	+", strArray="+ strArray +", views="+ views +"]";
		}
	}
	
	/**
	 * Ajoute une nouvelle propriété (chaine de caractère)
	 * @param categorie Catégorie à laquelle doit être rattaché la propriété 
	 * @param nomPropriete nom de la propriété
	 * @param valeurParDefaut valeur par défaut de la propriété
	 */
	public void ajouterStr(String categorie, String nomPropriete, String valeurParDefaut) {
		assert (!exists(categorie, nomPropriete,STRING_PROP)) : messageAssertion(categorie, nomPropriete, STRING_PROP, true, this);
		Category cat = getCategorie(categorie);
		cat.add(nomPropriete, STRING_PROP);
		cat.str.put(nomPropriete, valeurParDefaut);
		cat.changed();
	}

	/**
	 * @param categorie
	 * @return la categorie demandée
	 */
	private Category getCategorie(String categorie) {
		if (categorie == null)
			return categories.get("default");
		else {
			Category cat = categories.get(categorie);
			if(cat == null)
				cat = new Category(categorie);
			return cat;
		}
	}

	
	/**
	 * Ajoute une nouvelle propriété (contenant plusieurs chaines de caractères)
	 * @param categorie Catégorie à laquelle doit être rattaché la propriété 
	 * @param nomPropriete nom de la propriété
	 * @param taille  nombre d'éléments de la propriété
	 * @param valeursParDefaut valeurs par défaut des éléments de la propriété
	 */
//	@SuppressWarnings("null") //min sur taille du tableau 
	public void ajouterTableauStr(String categorie, String nomPropriete, int taille, String[] valeursParDefaut) {
		assert (!exists(categorie, nomPropriete, STRING_ARRAY_PROP)) : messageAssertion(categorie, nomPropriete, STRING_ARRAY_PROP, true, this); 
		//enregistrement des valeur par defaut
		String [] value = new String[taille];
		//prise en compte de la taille des deux tableaux
		int min = (valeursParDefaut == null) ? 0 : Math.min(taille, valeursParDefaut.length);
		for (int i = 0; i < min; i++)
			value[i] = valeursParDefaut[i];
//		ajout à la categorie
		Category cat = getCategorie(categorie);
		cat.add(nomPropriete, STRING_ARRAY_PROP);
//		initialisation de la propriété
		cat.strArray.put(nomPropriete, value);
		cat.changed();
	}

	/**
	 * Ajoute une nouvelle propriété représentant un choix parmi plusieurs possibilités.
	 * @param categorie catégorie à laquelle doit être rattaché la propriété 
	 * @param nomPropriete nom de la propriété
	 * @param valeurs index du choix par default
	 * @param choixParDefaut indice du choix par defaut
	 */
	public void ajouterChoix(String categorie, String nomPropriete,  String[] valeurs, int choixParDefaut) {
		assert (!exists(categorie, nomPropriete, CHOICE_PROP)) : messageAssertion(categorie, nomPropriete, CHOICE_PROP, true, this); 
//		ajout à la categorie
		Category cat = getCategorie(categorie);
		cat.add(nomPropriete, CHOICE_PROP);
//		initialisation de la propriété
		cat.choices.put(nomPropriete, new Choice(valeurs, choixParDefaut));
		cat.changed();
	}
	

	/**
	 * Ajoute une nouvelle propriété (Entier)
	 * @param categorie catégorie à laquelle doit être rattaché la propriété 
	 * @param nomPropriete nom de la propriété
	 * @param valeurParDefaut valeur par défaut de la propriété
	 */
	public void ajouterInt(String categorie, String nomPropriete, int valeurParDefaut) {
		assert (!exists(categorie, nomPropriete, INTEGER_PROP)) : messageAssertion(categorie, nomPropriete, INTEGER_PROP, true, this);
		Category cat = getCategorie(categorie);
		cat.add(nomPropriete, INTEGER_PROP);
		cat.entiers.put(nomPropriete, valeurParDefaut);
		cat.changed();
	}
	
	/**
	 * Ajoute une nouvelle propriété (contenant plusieurs entiers)
	 * @param categorie Catégorie à laquelle doit être rattaché la propriété 
	 * @param nomPropriete Nom de la propriété
	 * @param taille  Nombre d'éléments de la propriété
	 * @param valeursParDefaut Valeurs par défaut des éléments de la propriété
	 */
//	@SuppressWarnings("null")
	public void ajouterTableauInt(String categorie, String nomPropriete, int taille, int[] valeursParDefaut) {
		assert (!exists(categorie, nomPropriete, INTEGER_ARRAY_PROP)) : messageAssertion(categorie, nomPropriete, INTEGER_ARRAY_PROP, true, this);
		//enregistrement des valeur par defaut
		int[] value = new int[taille];
		//prise en compte de la taille des deux tableaux
		int min = (valeursParDefaut == null) ? 0 : Math.min(taille, valeursParDefaut.length);
		for (int i= 0; i < min; i++)
			value[i] = valeursParDefaut[i];
//		ajout à la categorie
		Category cat = getCategorie(categorie);
		cat.add(nomPropriete, INTEGER_ARRAY_PROP);
//		initialisation de la propriété
		cat.intArray.put(nomPropriete, value);
		cat.changed();
	}
	

	
	
	private boolean exists(String categorie, String nomPropriete, int typeProp) {
		assert (nomPropriete != null) : "La propriété doit avoir un nom, ici le nom n'est pas initialisé (null)";
		if (categorie == null)
			categorie = "default";
		Category cat = categories.get(categorie);
		if (cat == null)
			return false;
		return cat.props.contains(new Prop(nomPropriete, typeProp));
	}

	/**
	 * @return
	 */
	public Iterator<String> iterCategories() {
		return categories.keySet().iterator();
	}

	/**
	 * @param Categorie
	 * @return
	 */
	public Iterator<Prop> iterProperties(String Categorie) {
		return categories.get(Categorie).props.iterator();
	}
	
	/**
	 * Renvoi la valeur de la propriété Propriete définie dans la catégorie categorie.
	 * @param categorie Nom de la categorie dans laquelle se trouve la propriété ("default" si null)
	 * @param propriete Nom de la Propriété
	 * @return valeur de la propriété.
	 */
	public int getIntProp(String categorie, String propriete) {
		assert (exists(categorie, propriete, INTEGER_PROP)) : messageAssertion(categorie, propriete, INTEGER_PROP, false, this);
		return getCategorie(categorie).entiers.get(propriete).intValue();
	}
	
	/**
	 * Renvoi la valeur de la propriété Propriete définie dans la catégorie categorie.
	 * @param categorie Nom de la categorie dans laquelle se trouve la propriété ("default" si null)
	 * @param propriete Nom de la Propriété
	 * @return valeur de la propriété (tableau d'entiers).
	 */
	public int[] getIntArrayProp(String categorie, String propriete) {
		assert (exists(categorie, propriete, INTEGER_ARRAY_PROP)) : messageAssertion(categorie, propriete, INTEGER_ARRAY_PROP, false, this);
		return getCategorie(categorie).intArray.get(propriete);
	}
	
	/**
	 * Renvoi la valeur de la propriété Propriete définie dans la catégorie categorie.
	 * @param categorie Nom de la categorie dans laquelle se trouve la propriété ("default" si null)
	 * @param propriete Nom de la Propriété
	 * @return valeur de la propriété (chaine de caractères).
	 */
	public String getStringProp(String categorie, String propriete) {
		assert (exists(categorie, propriete, STRING_PROP)) : messageAssertion(categorie, propriete, STRING_PROP, false, this);
		return getCategorie(categorie).str.get(propriete);
	}

	/**
	 * Renvoi la valeur de la propriété Propriete définie dans la catégorie categorie.
	 * @param categorie Nom de la categorie dans laquelle se trouve la propriété ("default" si null)
	 * @param propriete Nom de la Propriété
	 * @return valeur de la propriété (tableau de chaine de caractères).
	 */
	public String[] getTableauStringProp(String categorie, String propriete) {
		assert (exists(categorie, propriete, STRING_ARRAY_PROP)) : messageAssertion(categorie, propriete, STRING_ARRAY_PROP, false, this);
		return getCategorie(categorie).strArray.get(propriete);
	}
	
	/**
	 * Renvoi la valeur de la propriété Propriete définie dans la catégorie categorie.
	 * @param categorie Nom de la categorie dans laquelle se trouve la propriété ("default" si null)
	 * @param propriete Nom de la Propriété
	 * @return valeur de la propriété (chaine de caractère séléctionnée).
	 */
	public String getChoixProp(String categorie, String propriete) {
		assert (exists(categorie, propriete, CHOICE_PROP)) : messageAssertion(categorie, propriete, CHOICE_PROP, false, this);
		return getCategorie(categorie).choices.get(propriete).selected();
	}

	
	/**
	 * Modifie la valeur (entier) de la propriété donnée 
	 * @param categorie Nom de la categorie dans laquelle se trouve la propriété ("default" si null)
	 * @param propriete Nom de la Propriété
	 * @param value Valeur de la propriété (entier).
	 */
	public void setIntProp(String categorie, String propriete, int value) {
		assert (exists(categorie, propriete, INTEGER_PROP)) : messageAssertion(categorie, propriete, INTEGER_PROP, false, this);
		getCategorie(categorie).entiers.put(propriete, value);
		modify(categorie, propriete, Proprietes.INTEGER_PROP);
	}
	
	/**
	/**
	 * Modifie la valeur (tableau d'entier) de la propriété donnée 
	 * @param categorie Nom de la categorie dans laquelle se trouve la propriété ("default" si null)
	 * @param propriete Nom de la Propriété
	 * @param value Valeur de la propriété (tableau d'entier).
	 */
	public void setIntArrayProp(String categorie, String propriete, int[] value) {
		assert (exists(categorie, propriete, INTEGER_ARRAY_PROP)) : messageAssertion(categorie, propriete, INTEGER_ARRAY_PROP, false, this);
		getCategorie(categorie).intArray.put(propriete, value);
		modify(categorie,propriete,Proprietes.STRING_ARRAY_PROP);
	}
	
	/**
	 * @param categorie
	 * @param propriete
	 * @param value 
	 */
	public void setStringProp(String categorie, String propriete,String value) {
		assert (exists(categorie, propriete, STRING_PROP)) : messageAssertion(categorie, propriete, STRING_PROP, false, this);
		getCategorie(categorie).str.put(propriete, value);
		modify(categorie,propriete,Proprietes.STRING_PROP);
	}

	/**
	 * @param categorie
	 * @param propriete
	 * @param value 
	 */
	public void setTableauStringProp(String categorie, String propriete, String[] value) {
		assert (exists(categorie, propriete, STRING_ARRAY_PROP)) : messageAssertion(categorie, propriete, STRING_ARRAY_PROP, false, this);
		getCategorie(categorie).strArray.put(propriete, value);
		modify(categorie,propriete,Proprietes.STRING_ARRAY_PROP);
	}
	
	/**
	 * @param categorie 
	 * @param propriete 
	 * @param value 
	 */
	public void setChoixProp(String categorie, String propriete,int value) {
		if (GameFrame.logLevel == 2)
			System.out.println(categorie +" - "+ propriete +" set Choix "+ value);
		assert (exists(categorie, propriete, CHOICE_PROP)) : messageAssertion(categorie, propriete, CHOICE_PROP, false, this);
		Choice choix = getCategorie(categorie).choices.get(propriete);
		assert (choix.s.length > value) : "La propriété "+ propriete +" de type choix ne contient pas le choix "+ value +" dans la catégorie "+ ((categorie == null) ? "default" : categorie);
		getCategorie(categorie).choices.get(propriete).select(value);
		modify(categorie, propriete, Proprietes.CHOICE_PROP);
	}

	/**
	 * @param categorie 
	 * @param propriete 
	 * @param value 
	 */
	public void setChoixProp(String categorie, String propriete,String value) {
		assert (exists(categorie, propriete, CHOICE_PROP)) : messageAssertion(categorie, propriete, CHOICE_PROP, false, this);
		if (GameFrame.logLevel >= 2)
			System.out.println(categorie +" - "+ propriete +" set Choix "+ value);
		Choice choix = getCategorie(categorie).choices.get(propriete);
		for (int i = 0; i < choix.s.length; i++) {
			if (choix.s.equals(value)) {
				choix.select(i);
				modify(categorie, propriete, Proprietes.CHOICE_PROP);
				return;
			}
		}
		assert false : "La propriété "+ propriete +" de type choix ne contient pas le choix "+ value +" dans la catégorie "+ ((categorie == null) ? "default" : categorie);
	}

	private static String stringOfType(int PROP_TYPE) {
		switch (PROP_TYPE) {
		case CHOICE_PROP:	
			return "Choix";
		case INTEGER_ARRAY_PROP:	
			return "tableau d'entiers";
		case INTEGER_PROP:	
			return "int";
		case STRING_ARRAY_PROP:	
			return "tableau de String";
		case STRING_PROP:	
			return "String";
		default:	
			return "inconnu";
		}
	}
	private static String messageAssertion(String categorie, String propriete, int PROP_TYPE, boolean exists, Proprietes prop) {
		return "in "+ prop +", La propriété "+ propriete +" de type "+ stringOfType(PROP_TYPE) +" "+ (exists ? "existe déjà" : "n'existe pas") +" dans la catégorie "+ ((categorie == null) ? "default" : categorie);
		
	}

	/**
	 * @param categorie
	 * @param propriete
	 * @return
	 */
	public Choice getChoix(String categorie, String propriete) {
		return this.getCategorie(categorie).choices.get(propriete);
	}

	/**
	 * @param categorie
	 * @param propriete
	 * @param PROP_TYPE
	 */
	public void modify(String categorie, String propriete, int PROP_TYPE) {
		if (GameFrame.logLevel >= 1)
			System.out.println(categorie +" - "+ propriete +" : "+ stringOfType(PROP_TYPE) +"modified");
		for (UpdateListener Ul : getCategorie(categorie).views) {
			Ul.updated(propriete,PROP_TYPE);
		} 
	}

	/**
	 * @param categorie
	 * @param propriete
	 * @param pos
	 * @param valeur
	 */
	public void setIntArrayProp(String categorie, String propriete,	int pos, int valeur) {
		if (GameFrame.logLevel >= 2)
			System.out.println("set int array position "+ pos +" valeur"+ valeur);
		getIntArrayProp(categorie, propriete)[pos] = valeur;
		modify(categorie, propriete, INTEGER_ARRAY_PROP);
		
	}

	/**
	 * @param categorie
	 * @param propriete
	 * @param pos
	 * @param valeur
	 */
	public void setTableauStringProp(String categorie, String propriete, int pos, String valeur) {
		if (GameFrame.logLevel >= 2)
			System.out.println("set TableauString position "+ pos +" valeur"+ valeur);
		getTableauStringProp(categorie, propriete)[pos] = valeur;
		modify(categorie, propriete, Proprietes.STRING_ARRAY_PROP);
		
	}

	/**
	 * Change la description de la catégorie, si celle-ci n'existait pas elle est créée
	 * @param name Nom de la categorie
	 * @param description Description de la categorie
	 */
	public void setDescriptionCategory(String name, String description) {
		Category cat = getCategorie(name);
		cat.description = description;
		cat.changed();
	}

	/**
	 * @param categorie
	 * @return la description de la categorie.
	 */
	public String getDescriptionCategorie(String categorie) {
		Category cat = getCategorie(categorie);
		return cat.description;
	}
	
	/**
	 * 	Donne une description de la propriété (affichée dans l'interface graphique)
	 * @param categorie categorie à laquelle est rattachée la propriété
	 * @param name Nom de la propriété
	 * @param TYPE_PROP type de propriété (à choisir parmi INTEGER_PROP, INTEGER_ARRAY_PROP, STRING_PROP, STRING_ARRAY_PROP, CHOICE_PROP. 
	 * @param description Description de la propriété
	 */
	public void setDescriptionPropriete(String categorie, String name, int TYPE_PROP, String description) {
		assert (exists(categorie, name, TYPE_PROP)) : messageAssertion(categorie, name, TYPE_PROP, false, this);
		Category cat = getCategorie(categorie);
		cat.descriptions.put(new Prop(name, TYPE_PROP), description);
		cat.changed();
		if (GameFrame.logLevel >= 2) {
			System.out.println(cat.descriptions);
		}
	}

	/**
	 * @param categorie
	 * @param name
	 * @param TYPE_PROP
	 * @return
	 */
	public String getDescriptionPropriete(String categorie, String name, int TYPE_PROP) {
		assert (exists(categorie, name, TYPE_PROP)) : messageAssertion(categorie, name, TYPE_PROP, false, this);
		return getCategorie(categorie).descriptions.get(new Prop(name, TYPE_PROP));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categories == null) ? 0 : categories.hashCode());
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
		Proprietes other = (Proprietes) obj;
		if (categories == null) {
			if (other.categories != null)
				return false;
		}
		else if (!categories.equals(other.categories))
			return false;
		return true;
	}

	public void addListener(CategoryVue categoryVue, String categorie) {
		getCategorie(categorie).views.add(categoryVue);
		
	}
	
	/**
	 * Clear all categories and their properties.
	 */
	public void clear() {
		categories.clear();
	}

	public String toString() {
		return categories.toString();			
	}
		
}
