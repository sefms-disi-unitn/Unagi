package it.unitn.disi.unagi.application.internal.services;

import it.unitn.disi.unagi.application.Activator;
import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.application.exceptions.UnagiExceptionType;
import it.unitn.disi.unagi.application.persistence.SerializationUnagiProjectPersistenceManager;
import it.unitn.disi.unagi.application.persistence.UnagiProjectPersistenceManager;
import it.unitn.disi.unagi.application.services.ManageProjectsService;
import it.unitn.disi.unagi.application.util.PluginLogger;
import it.unitn.disi.unagi.domain.core.UnagiProject;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * Implementation of the "Manage Project" service.
 *
 * @see it.unitn.disi.unagi.application.services.ManageProjectService
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class ManageProjectServiceBean implements ManageProjectsService {
	/** The plug-in logger. */
	private static final PluginLogger logger = new PluginLogger(Activator.getInstance().getLog(), Activator.PLUGIN_ID);

	/** List of open projects. */
	private SortedSet<UnagiProject> openProjects = new TreeSet<UnagiProject>();
	
	/** Persistence manager for UnagiProject objects. */
	private UnagiProjectPersistenceManager persistenceManager = new SerializationUnagiProjectPersistenceManager();
	
	/** List of listeners for property changes. */
	private Set<IPropertyChangeListener> listeners = new HashSet<IPropertyChangeListener>();

	/** @see it.unitn.disi.unagi.application.services.ManageProjectsService#createNewProject(java.lang.String, java.io.File) */
	@Override
	public UnagiProject createNewProject(String name, File folder) throws UnagiException {
		logger.info("Creating a new project called \"{0}\" using folder \"{1}\"...", name, folder); //$NON-NLS-1$
		
		// Creates the new project.
		UnagiProject project = new UnagiProject(folder, name);
		
		// Updates the timestamp of the last save.
		project.updateSaveTimestamp();
		
		// Immediately saves the project to the file selected by the user.
		try {
			persistenceManager.save(project, folder);
		}
		catch (IOException e) {
			logger.error(e, "Could not save the new project"); //$NON-NLS-1$
			throw new UnagiException(UnagiExceptionType.COULD_NOT_SAVE_UNAGI_PROJECT, e);
		}
		
		// Adds the project to the list of open projects.
		SortedSet<UnagiProject> previousOpenProjects = new TreeSet<UnagiProject>(openProjects);
		openProjects.add(project);
		
		// Notifies listeners of a property change and returns.
		notifyListeners(previousOpenProjects);
		return project;
	}

	/** @see it.unitn.disi.unagi.application.services.ManageProjectsService#saveProject(it.unitn.disi.unagi.domain.core.UnagiProject) */
	@Override
	public void saveProject(UnagiProject project) throws UnagiException {
		logger.info("Saving project \"{0}\" ...", project.getName()); //$NON-NLS-1$
		
		// Updates the timestamp of the last save.
		project.updateSaveTimestamp();

		// Saves the project to its file.
		try {
			persistenceManager.save(project, project.getFolder());
		}
		catch (IOException e) {
			logger.error(e, "Could not save the project"); //$NON-NLS-1$
			throw new UnagiException(UnagiExceptionType.COULD_NOT_SAVE_UNAGI_PROJECT, e);
		}

		// Notifies listeners of a property change.
		notifyListeners(openProjects);
	}

	/** @see it.unitn.disi.unagi.application.services.ManageProjectsService#isProjectFolder(java.io.File) */
	@Override
	public boolean isProjectFolder(File folder) {
		boolean result = persistenceManager.checkProjectFolder(folder);
		logger.info("Checking if \"{0}\" is a project folder returns: {1}.", folder, result); //$NON-NLS-1$
		return result;
	}

	/** @see it.unitn.disi.unagi.application.services.ManageProjectsService#loadProject(java.io.File) */
	@Override
	public UnagiProject loadProject(File folder) throws UnagiException {
		logger.info("Loading a project from folder \"{0}\" ...", folder); //$NON-NLS-1$

		// Loads the project from the file.
		UnagiProject project = null;
		try {
			project = persistenceManager.load(folder);
		}
		catch (Exception e) {
			logger.error(e, "Could not load project from folder"); //$NON-NLS-1$
			throw new UnagiException(UnagiExceptionType.COULD_NOT_LOAD_UNAGI_PROJECT);
		}
		
		// In case the user has changed the name/location of the folder, update the UnagiProject object.
		project.setFolder(folder);
		
		// Adds the project to the list of open projects.
		SortedSet<UnagiProject> previousOpenProjects = new TreeSet<UnagiProject>(openProjects);
		openProjects.add(project);
		
		// Notifies listeners of a property change and returns.
		notifyListeners(previousOpenProjects);
		return project;
	}

	/** @see it.unitn.disi.unagi.application.services.ManageProjectsService#closeProject(it.unitn.disi.unagi.domain.core.UnagiProject) */
	@Override
	public void closeProject(UnagiProject project) {
		logger.info("Closing project \"{0}\" ...", project.getName()); //$NON-NLS-1$

		// Removes the project from the open projects list.
		SortedSet<UnagiProject> previousOpenProjects = new TreeSet<UnagiProject>(openProjects);
		openProjects.remove(project);
		
		// Notifies listeners of a property change.
		notifyListeners(previousOpenProjects);
	}

	/** @see it.unitn.disi.unagi.application.services.ManageProjectsService#getOpenProjects() */
	@Override
	public Set<UnagiProject> getOpenProjects() {
		return openProjects;
	}

	/** @see it.unitn.disi.unagi.application.services.ManageProjectsService#addPropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener) */
	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		logger.info("Adding instance of class {0} as listener for project-related operations...", listener.getClass()); //$NON-NLS-1$
		listeners.add(listener);
	}

	/** @see it.unitn.disi.unagi.application.services.ManageProjectsService#removePropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener) */
	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		logger.info("Removing instance of class {0} as listener for project-related operations...", listener.getClass()); //$NON-NLS-1$
		listeners.remove(listener);
	}

	/**
	 * Notifies all listeners for property changes in the list of open projects that the list has been updated. This gives
	 * other views (e.g., the Open Projects view) a chance of updating their UI. 
	 * 
	 * @param previousOpenProjects
	 * 		The list of open projects previous to the latest changes, in case any listener wants to compare with the new list
	 * 		and process only the newly opened projects.
	 * 
	 * @see it.unitn.disi.unagi.gui.views.ProjectsView
	 */
	private void notifyListeners(SortedSet<UnagiProject> previousOpenProjects) {
		logger.info("Notifying all listeners of a change in the list of open projects..."); //$NON-NLS-1$

		for (IPropertyChangeListener listener : listeners) {
			listener.propertyChange(new PropertyChangeEvent(this, "openProjects", previousOpenProjects, openProjects)); //$NON-NLS-1$
		}
	}
}
