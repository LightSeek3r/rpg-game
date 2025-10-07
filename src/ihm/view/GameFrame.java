package ihm.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import ihm.controller.Controller;
import ihm.controller.Evenement;
import ihm.model.Board;
import ihm.model.Proprietes;
import ihm.view.propriete.ProprietesVue;

import javax.swing.*;



/**
 * @author Julien Tesson
 *
 */
public class GameFrame extends JFrame implements ActionListener {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -7145665607704631937L;

	private static final String LOAD = "load";
	public static int logLevel = 0;
	
	Map<Proprietes,ProprietesVue> printedPropVue;
	BoardLayers boardview;
	
	Board boardmodel;
	
	Controller boardcontroller;
	
	JPanel printPanel;
	JLabel bottomMessageLabel;
	
	private void initIHM() {
		assert (boardmodel != null && boardview != null && boardcontroller != null);
		this.addKeyListener(boardcontroller);
		boardview.addMouseListener(boardcontroller);
		JScrollPane boardviewScroll = new JScrollPane(boardview);
		boardviewScroll.setMinimumSize(boardview.getMinimumSize());
		add(boardviewScroll, BorderLayout.CENTER);
		add(new JScrollPane(printPanel), BorderLayout.EAST);
		
		// Initialize bottom message bar
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomMessageLabel = new JLabel("Bienvenue dans ce donjon !");
		bottomMessageLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		bottomMessageLabel.setOpaque(true);
		bottomMessageLabel.setBackground(new java.awt.Color(240, 240, 240));
		bottomPanel.add(bottomMessageLabel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		
		setFocusable(true);	
	}
	
	
	/**
	 * @param largeur
	 * @param hauteur
	 */
	public GameFrame(int largeur, int hauteur) {
		super("Mini-projet L1");
		setSize(33 * largeur + 1, 33 * hauteur  + 3);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        boardmodel = new Board(largeur, hauteur);
        boardview = new BoardLayers(boardmodel);
        boardcontroller = new Controller(boardmodel, boardview);
        printPanel = new JPanel();
        printPanel.setLayout(new BoxLayout(printPanel, BoxLayout.Y_AXIS));
        printedPropVue = new HashMap<Proprietes,ProprietesVue>();
        initIHM();
        
	}

	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(LOAD)) {
//			new LoadingDialog(this,true).setVisible(true); 
		}
		
	}
	

	public BoardBackground getBoardBackground() {
		return boardview.getBoardBackground();
	}

	public BoardSelector getBoardSelector() {
		return boardview.getBoardSelector();
	}

	public BoardSprites getBoardSprites() {
		return boardview.getBoardSprites();
	}


	public Evenement readEvent() {
		return boardcontroller.getNextEvent();		
	}


	public Evenement readEvent(long timeout) {
		return boardcontroller.getNextEvent(timeout);		
	}




	public int readInt(String title, String question) {
		int result;
		try {
			String request = JOptionPane.showInputDialog(this, question, title, JOptionPane.QUESTION_MESSAGE);
			result = Integer.parseInt(request);
		}
		catch (java.lang.NumberFormatException e) {
				return readInt(title, question +"\nEntrez un entier");
		}
		return result;
	}




	/**
	 * Affiche la Propriété dans le panneau lateral
	 * @param p
	 * @param visible
	 */
	public void print(Proprietes p, boolean visible) {
		if (!visible && printedPropVue.containsKey(p))
			printPanel.remove(printedPropVue.get(p));
		else if (visible && ! printedPropVue.containsKey(p)) {
			ProprietesVue pv = new ProprietesVue(p);
			printedPropVue.put(p, pv);
			printPanel.add(pv);
		}
		printPanel.revalidate();
		printPanel.repaint();
	}
	
	/**
	 * Clear all properties from the side panel.
	 */
	public void clearPrintPanel() {
		printPanel.removeAll();
		printedPropVue.clear();
		printPanel.revalidate();
		printPanel.repaint();
	}
	
	/**
	 * Pack the frame to fit all components properly.
	 */
	public void packFrame() {
		this.pack();
	}
	
	/**
	 * Set the message to display in the bottom bar.
	 * @param message The message to display.
	 */
	public void setBottomMessage(String message) {
		if (bottomMessageLabel != null) {
			java.awt.EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					bottomMessageLabel.setText(message);
				}
			});
		}
	}



	public void setLog(int i) {
		logLevel = i;
	}

}




