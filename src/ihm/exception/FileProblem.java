package ihm.exception;

/**
 * Exception pour les fichiers (lecture/écriture/formatage)
 * @see Exception
 * @author Matthieu Lopez
 *
 */
public class FileProblem extends Exception {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -1064268354552272583L;
	
	/**
	 * Nom du fichier concerné
	 */
	String filename;

	public FileProblem(String filename, String message) {
		super(message);
		this.filename=filename;
	}
	
	public String getMessage(){
		return filename+" : "+super.getMessage();
	}

}
