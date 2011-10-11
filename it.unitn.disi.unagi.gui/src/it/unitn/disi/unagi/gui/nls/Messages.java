package it.unitn.disi.unagi.gui.nls;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Utility class created by Eclipse to access the resource bundle that externalizes the strings from the plugin's
 * classes.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class Messages {
	private static final String BUNDLE_NAME = "it.unitn.disi.unagi.gui.nls.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private Messages() {}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		}
		catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
