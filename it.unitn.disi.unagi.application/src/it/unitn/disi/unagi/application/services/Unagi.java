package it.unitn.disi.unagi.application.services;

import it.unitn.disi.unagi.application.Activator;
import it.unitn.disi.unagi.application.internal.services.ManageModelsServiceBean;
import it.unitn.disi.unagi.application.internal.services.ManageProjectServiceBean;
import it.unitn.disi.unagi.application.util.PluginLogger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Singleton class that represents the whole application and centralizes access to the application's services.
 * 
 * FIXME: exception handling could also be improved throughout the system.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class Unagi {
	/** The plug-in logger. */
	private static final PluginLogger logger = new PluginLogger(Activator.getInstance().getLog(), Activator.PLUGIN_ID);

	/** Key for the configuration "Last folder used in file dialogs". */
	public static final String CFG_LAST_FOLDER_FILE_DIALOGS = "last-folder-file-dialogs"; //$NON-NLS-1$
	
	/** Key for the configuration "Project sub-directory where models are stored. */
	public static final String CFG_PROJECT_SUBDIR_MODELS = "project-subdir-models"; //$NON-NLS-1$

	/** Singleton instance of the class. */
	private static final Unagi instance = new Unagi();

	/** Application configuration. */
	private Map<String, String> configurationMap = new HashMap<String, String>();

	/** Default implementation for the "Manage Project" service. */
	private ManageProjectsService manageProjectsService;

	/** Default implementation for the "Manage Models" service. */
	private ManageModelsService manageModelsService;

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
		logger.info("Initializing the Unagi application..."); //$NON-NLS-1$
		
		// Initializes the default implementations for the services. 
		// This initialization is done here because some of these services need to refer back to this instance.
		// TODO: remove "hard-coded" selection of service implementation and use some kind of configuration or DI.
		manageProjectsService = new ManageProjectServiceBean();
		manageModelsService = new ManageModelsServiceBean(this);
		
		// Sets the default values for the "Last folder used in file dialogs" configuration.
		Properties systemProps = System.getProperties();
		String userHome = systemProps.getProperty("user.home"); //$NON-NLS-1$
		if ((userHome == null) || userHome.isEmpty())
			userHome = "."; //$NON-NLS-1$
		configurationMap.put(CFG_LAST_FOLDER_FILE_DIALOGS, new File(userHome).getAbsolutePath());
		
		// Sets the default values for the project sub-directories.
		configurationMap.put(CFG_PROJECT_SUBDIR_MODELS, "models"); //$NON-NLS-1$
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

	/** Getter for manageModelsService. */
	public ManageModelsService getManageModelsService() {
		return manageModelsService;
	}
}
