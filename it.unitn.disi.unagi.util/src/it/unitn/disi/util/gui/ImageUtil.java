package it.unitn.disi.util.gui;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

/**
 * Class that offers image utility services through static methods.
 *
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public final class ImageUtil {
	/** Image cache. Avoids loading twice the same image from disk. */
	private static Map<String, Image> imageCache = new HashMap<String, Image>();
	
	/** This class is not meant to have objects. */
	private ImageUtil() {}
	
	/**
	 * Loads an image from a file that is located within the plug-in.
	 * 
	 * @param relativePath
	 * 		Path to the image relative to the root of the plug-in.
	 * 
	 * @return
	 * 		An SWT Image object that represents the image.
	 */
	public static Image loadImage(Bundle bundle, String relativePath) {
		Image image = null;
		
		// Checks if the image is in the cache first.
		image = imageCache.get(relativePath);
		if (image != null) return image;
		
		// Attempts to load the image, given the relative path.
		try {
			URL url = FileLocator.find(bundle, new Path(relativePath), Collections.EMPTY_MAP);
			File iconFile = new File(FileLocator.toFileURL(url).getPath());
			image = new Image(Display.getCurrent(), iconFile.getPath());
			
			// Places the image in the cache.
			imageCache.put(relativePath, image);
		}
		catch (IOException e) {
			e.printStackTrace();
			
			// Falls back to the shared object icon from Eclipse.
			image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
		}
		
		return image;
	}
}
