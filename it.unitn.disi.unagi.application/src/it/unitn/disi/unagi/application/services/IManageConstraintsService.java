package it.unitn.disi.unagi.application.services;

import it.unitn.disi.unagi.application.exceptions.CouldNotCreateFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotDeleteFileException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * TODO: document this type.
 *
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public interface IManageConstraintsService extends IManageFilesService {
	/** File extension for constraint files. */
	String CONSTRAINTS_FILE_EXTENSION = "ocl"; //$NON-NLS-1$

	IFile createNewConstraintsFile(IProgressMonitor progressMonitor, IProject project, String name) throws CouldNotCreateFileException;
	
	void deleteConstraintsFile(IProgressMonitor progressMonitor, IFile constraintsFile) throws CouldNotDeleteFileException;
}
