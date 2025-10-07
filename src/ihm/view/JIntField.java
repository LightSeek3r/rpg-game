package ihm.view;

import javax.swing.JTextField;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * @author Julien Tesson
 *
 */
public class JIntField extends JTextField {

	/**
	 * 
	 * This is a component that provides a textfield that accepts only
	 * integer values. derviced from the one of Andreas Schmitz
	 * http://www.koders.com/java/fid3877707475108225A3DDC22C28EA6EFFC526D847.aspx
	 * @version 31 mars 2010
	 */

	private static final long serialVersionUID = 8871055047813217005L;

	/**
	 * 
	 * @param defaut valeur par defaut
	 * @param col nb colone du champ
	 */
	public JIntField(int defaut, int col){
		super(Integer.toString(defaut), col);   
		CaretListener caretupdate = new CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent e) {
                JTextField text = (JTextField)e.getSource();
                if (GameFrame.logLevel >=3){ System.out.println(text.getText());}
            }
        };
        this.addCaretListener(caretupdate);
		
	} 

	/**
     * Overrides something in JTextField; it just returns an
     * IntDocument (This <i>IS</i> a JIntField...)
     *@return returns the IntDocument
     */
    @Override
	protected Document createDefaultModel(){
	return new IntDocument();
    }

    /**
     * Returns the actual value.
     *@return the value
     */
    public int getValue(){
	try{
		if (getText().length() == 0) 
			return 0;
		else	
			return Integer.parseInt(getText());
	}catch(NumberFormatException e){
	    assert false : getText()+" has to be an integer";
		return Integer.MIN_VALUE;
	}
    }

    /**
     * Sets the actual value.
     *@param val the value
     */
    public void setValue( int val ){
	setText(""+val);
    }
    private class IntDocument extends PlainDocument{
	/**
		 * 
		 */
		private static final long serialVersionUID = 4353158774465574281L;

	@Override
	public void insertString( int offs, String s, AttributeSet a )
	throws BadLocationException{
	    char[] src=s.toCharArray();
	    char[] res=new char[src.length];
	    int j=0;
	    for(int i=0;i<src.length;i++){
		if(Character.isDigit(src[i])) res[j++]=src[i];
	    }
	    super.insertString( offs, new String( res, 0, j), a);
	}

    }


	
	
	
}
