/**
 * 
 */
package ihm.view;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Gestion du cache des images.
 * @author Julien Tesson
 *
 */
public class ImageManager {
	protected static HashMap<URL,Image> map = new HashMap<URL,Image>(10);
	
	
	public ImageManager() {
		super();
		map = new HashMap<URL,Image>(10);
	}


	public static Image getImage(String filename) throws FileNotFoundException {
		Image img;
		URL url;
		try {
			if (GameFrame.logLevel >= 2)
				System.out.println("getting image "+ filename);
			File f = new File (filename);
			if (GameFrame.logLevel >= 2)
				System.out.println("getting image "+ f);
			File path = new File (".");
			if (GameFrame.logLevel >= 2)
				System.out.println("image exists ?"+ f.exists());
			if(!f.exists())
				throw new FileNotFoundException("le fichier "+ filename +" n'existe pas dans le répertoire "+ path.getAbsolutePath());
			url = f.toURI().toURL();
			img = map.get(url);
			if (img == null) {
				img = java.awt.Toolkit.getDefaultToolkit().createImage(filename);
				map.put(url, img);
			}
		return img;
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException("erreur de chargement d'image, \""+ filename +"\" n'est pas un  nom de fichier valable");
		}
	}

}
