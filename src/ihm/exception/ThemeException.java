package ihm.exception;

/**
 * Exception pour ce qui concerne les thèmes 
 * @see Exception
 * @author Matthieu Lopez
 *
 */
public class ThemeException extends Exception{

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -7443154929765483651L;
	
	public static final int UNSUPPORTED_SIZE=0;
	public static final int FILE_ERROR=1;

	
	public int getCode() {
		return code;
	}

	int code;
	
	private ThemeException(int code, String message){
		super(message);
		this.code=code;
	}
	
	public static ThemeException unsupportedSize(String filename ){
		return new ThemeException(UNSUPPORTED_SIZE,"Le fichier "+filename+" n'a pas la bonne dimension (64x64).");
	}
	
	public static ThemeException fileError(String filename ){
		return new ThemeException(FILE_ERROR,"Erreur lors de la lecture du fichier "+filename+".");
	}
}
