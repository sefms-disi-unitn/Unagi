package it.unitn.disi.unagi.application.internal.services;

import it.unitn.disi.unagi.application.exceptions.CouldNotLoadUnagiProjectException;
import it.unitn.disi.unagi.application.exceptions.CouldNotSaveUnagiProjectException;
import it.unitn.disi.unagi.application.persistence.SerializationUnagiProjectPersistenceManager;
import it.unitn.disi.unagi.application.persistence.UnagiProjectPersistenceManager;
import it.unitn.disi.unagi.application.services.ManageProjectsService;
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
	/** List of open projects. */
	private SortedSet<UnagiProject> openProjects = new TreeSet<UnagiProject>();
	
	/** Persistence manager for UnagiProject objects. */
	private UnagiProjectPersistenceManager persistenceManager = new SerializationUnagiProjectPersistenceManager();
	
	/** List of listeners for property changes. */
	private Set<IPropertyChangeListener> listeners = new HashSet<IPropertyChangeListener>();

	/** @see it.unitn.disi.unagi.application.services.ManageProjectsService#createNewProject(java.lang.String, java.io.File) */
	@Override
	public UnagiProject createNewProject(String name, File folder) throws CouldNotSaveUnagiProjectException {
		// Creates the new project.
		UnagiProject project = new UnagiProject(folder, name);
		
		// Immediately saves the project to the file selected by the user.
		try {
			persistenceManager.save(project, folder);
		}
		catch (IOException e) {
			throw new CouldNotSaveUnagiProjectException(e);
		}
		
		// Updates the timestamp of the last save.
		project.updateSaveTimestamp();
		
		// Adds the project to the list of open projects.
		SortedSet<UnagiProject> previousOpenProjects = new TreeSet<UnagiProject>(openProjects);
		openProjects.add(project);
		
		// Notifies listeners of a property change and returns.
		notifyListeners(previousOpenProjects);
		return project;
	}

	/** @see it.unitn.disi.unagi.application.services.ManageProjectsService#saveProject(it.unitn.disi.unagi.domain.core.UnagiProject) */
	@Override
	public void saveProject(UnagiProject project) throws CouldNotSaveUnagiProjectException {
		// Saves the project to its file.
		try {
			persistenceManager.save(project, project.getFolder());
		}
		catch (IOException e) {
			throw new CouldNotSaveUnagiProjectException(e);
		}
		
		// Updates the timestamp of the last save.
		project.updateSaveTimestamp();

		// Notifies listeners of a property change.
		notifyListeners(openProjects);
	}

	/** @see it.unitn.disi.unagi.application.services.ManageProjectsService#isProjectFolder(java.io.File) */
	@Override
	public boolean isProjectFolder(File folder) {
		return persistenceManager.checkProjectFolder(folder);
	}

	/** @see it.unitn.disi.unagi.application.services.ManageProjectsService#loadProject(java.io.File) */
	@Override
	public UnagiProject loadProject(File folder) throws CouldNotLoadUnagiProjectException {
		// Loads the project from the file.
		UnagiProject project = null;
		try {
			project = persistenceManager.load(folder);
		}
		catch (Exception e) {
			throw new CouldNotLoadUnagiProjectException(e);
		}
		
		// In case the user has changed the name/location of the folder, update the UnagiProject object.
		project.setFolder(folder);
		
		// Updates the timestamp of the last save.
		project.updateSaveTimestamp();
		
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
		listeners.add(listener);
	}

	/** @see it.unitn.disi.unagi.application.services.ManageProjectsService#removePropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener) */
	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
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
		for (IPropertyChangeListener listener : listeners) {
			listener.propertyChange(new PropertyChangeEvent(this, "openProjects", previousOpenProjects, openProjects)); //$NON-NLS-1$
		}
	}
}
