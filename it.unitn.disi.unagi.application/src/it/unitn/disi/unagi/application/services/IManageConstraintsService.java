package it.unitn.disi.unagi.application.services;

import it.unitn.disi.unagi.application.exceptions.CouldNotCreateFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotDeleteFileException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

/**
 * TODO: document this type.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public interface IManageConstraintsService extends IManageFilesService {
	/** File extension for constraint files. */
	String CONSTRAINTS_FILE_EXTENSION = "ocl"; //$NON-NLS-1$

	/** TODO: document this field. */
	IPath CONSTRAINTS_TEMPLATE_FILE_PATH = new Path("META-INF/template.ocl"); //$NON-NLS-1$;
	
	/** TODO: document this field. */
	String CONSTRAINTS_VARIABLE_PACKAGE_NAME = "packageName"; //$NON-NLS-1$

	/**
	 * TODO: document this method.
	 * 
	 * @param progressMonitor
	 * @param project
	 * @param name
	 * @return
	 * @throws CouldNotCreateFileException
	 */
	IFile createNewConstraintsFile(IProgressMonitor progressMonitor, IProject project, String name) throws CouldNotCreateFileException;

	/**
	 * TODO: document this method.
	 * 
	 * @param progressMonitor
	 * @param constraintsFile
	 * @throws CouldNotDeleteFileException
	 */
	void deleteConstraintsFile(IProgressMonitor progressMonitor, IFile constraintsFile) throws CouldNotDeleteFileException;
}
