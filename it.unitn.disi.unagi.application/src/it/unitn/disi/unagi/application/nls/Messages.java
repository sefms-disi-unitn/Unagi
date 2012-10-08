package it.unitn.disi.unagi.application.nls;

import it.unitn.disi.util.logging.LogUtil;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Utility class that provides (static) methods for retrieving formatted and non-formatted strings from a resource
 * bundle (.properties) file in order to implement internationalization in the bundle.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class Messages {
	/** Base name of the resource bundle that contains the internationalized strings. */
	private static final String BUNDLE_NAME = "it.unitn.disi.unagi.application.nls.messages"; //$NON-NLS-1$

	/** Resource bundle object that is used to load the strings from the .properties file. */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * Retrieves a non-formatted string, given its key.
	 * 
	 * @param key
	 *          The key used to locate the string in the resource bundle.
	 * @return The internationalized non-formatted string retrieved from the bundle, or !key! if it's not found.
	 */
	public static String getString(String key) {
		LogUtil.log.debug("Retrieving i18n message for key: {0}", key); //$NON-NLS-1$

		try {
			return RESOURCE_BUNDLE.getString(key);
		}
		catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	/**
	 * Retrieves a formatted string, given its key and the parameters to format it.
	 * 
	 * @param key
	 *          The key used to locate the string in the resource bundle.
	 * @param params
	 *          An array (varargs) of objects that will substitute placeholders {0}, {1}, etc. according to their position
	 *          in the array.
	 * @return The internationalized formatted string retrieved from the bundle, or !key! if it's not found.
	 */
	public static String getFormattedString(String key, Object ... params) {
		String pattern = getString(key);
		return MessageFormat.format(pattern, params);
	}
}
