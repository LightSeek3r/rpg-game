package base;

import javax.swing.*;
import java.awt.* ;
import java.awt.event.* ;

// Menu principal - inachevé

public class Menu{ 

    public java.util.Scanner in = new java.util.Scanner(System.in);
    public java.util.Scanner sc = new java.util.Scanner(System.in);

    public int [] personnage = new int [1];

    // personnage[0] : 0 : warrior / 1 : archer / 2 : mage 
    
    // a ajouter : nouvelle partie, importer, (sauvegarder : in game) quitter

    
    public static String  nom()
    {
    	String nom = JOptionPane.showInputDialog("Veuillez entrer un pseudo !");
    	System.out.println(nom);
    	return nom;
    }
    public static void main()
    { 
    	type();
    	nom();
    }
    public static void type()
    {
    	Fenetre fen = new Fenetre() ;
        fen.setVisible(true) ;
    }
}


class Fenetre extends JFrame implements ActionListener
{ 
	String[] type = { "Warrior", "Archer", "Mage" } ;
	public Fenetre()
	{ 
		setTitle ("Choix type de heros") ;
		setSize (400, 220) ;
		JButton saisie = new JButton ("Go !") ;
		getContentPane().add(saisie, "South") ;
		saisie.addActionListener (this) ;
	}
	public void actionPerformed (ActionEvent e)
	{ 
		System.out.println ("** affichage boite d'options") ;
		String txt = (String)JOptionPane.showInputDialog (this,
                        "Choisissez un type de personnage", "BOITE D'OPTIONS",
                        JOptionPane.QUESTION_MESSAGE, null, type, type[1]) ;
		//if (txt == null) System.out.println (" pas de choix effectue") ;
		//else System.out.println (" option choisie :" + txt) ;
	}
}


