package it.unitn.disi.unagi.application.services;

import it.unitn.disi.unagi.application.internal.services.ManageProjectServiceBean;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Singleton class that represents the whole application and centralizes access to the application's services.
 * 
 * FIXME: all the classes need externalization of Strings.
 * FIXME: all the classes need logging.
 * FIXME: exception handling could also be improved throughout the system.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class Unagi {
	/** Key for the configuration "Last folder used in file dialogs". */
	public static final String CFG_LAST_FOLDER_FILE_DIALOGS = "last-folder-file-dialogs";

	/** Singleton instance of the class. */
	private static final Unagi instance = new Unagi();

	/** Application configuration. */
	private Map<String, String> configurationMap = new HashMap<String, String>();

	/** Default implementation for the "Manage Project" service. */
	private ManageProjectsService manageProjectsService = new ManageProjectServiceBean();

	/** Private constructor. */
	private Unagi() {
		init();
	}

	/**
	 * Returns the singleton instance of the Unagi application.
	 * 
	 * @return The singleton instance of the Unagi application.
	 */
	public static Unagi getInstance() {
		return instance;
	}

	/**
	 * Initializes the singleton instance of the Unagi application by reading some contextual information and building the
	 * system's configuration.
	 */
	private void init() {
		// Sets the default values for the "Last folder used in file dialogs" configuration.
		Properties systemProps = System.getProperties();
		String userHome = systemProps.getProperty("user.home");
		if ((userHome == null) || userHome.isEmpty())
			userHome = ".";
		configurationMap.put(CFG_LAST_FOLDER_FILE_DIALOGS, new File(userHome).getAbsolutePath());
	}

	/**
	 * Obtains the value for some system configuration, given its key.
	 * 
	 * @param key
	 *          The key that represents the system configuration.
	 * 
	 * @return The value of the system configuration related to the given key, or <code>null</code> if the key doesn't
	 *         exist in the configuration.
	 */
	public String getProperty(String key) {
		return configurationMap.get(key);
	}

	/**
	 * Sets the value for some system configuration, given also its key.
	 * 
	 * @param key
	 *          The key that represents the system configuration.
	 * @param value
	 *          The new value that will be associated with the given key.
	 */
	public void setProperty(String key, String value) {
		configurationMap.put(key, value);
	}

	/** Getter for property: manageProjectsService. */
	public ManageProjectsService getManageProjectsService() {
		return manageProjectsService;
	}
}
