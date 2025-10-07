package ihm.exception;

/**
 * Exception pour ce qui concerne les cartes 
 * @see Exception
 * @author Matthieu Lopez
 *
 */
public class MapException extends Exception{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -4053991211233435333L;
	
	private MapException(String message){
		super(message);
	}
	
	public static MapException outOfBounds(int x, int y){
		return new MapException("("+x+","+y+") en dehors des bornes de la carte");
	}
	
	public static MapException loading(String message){
		return new MapException("loading of the map : "+message);
	}
	
	public static MapException noMoreFreePositions(){
		return new MapException("Pas suffisament de place libre sur la carte");
	}
}
