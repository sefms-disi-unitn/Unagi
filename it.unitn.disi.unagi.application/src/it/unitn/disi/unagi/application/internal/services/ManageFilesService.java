package it.unitn.disi.unagi.application.internal.services;

import it.unitn.disi.unagi.application.exceptions.CouldNotCreateFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotDeleteFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotReadFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotSaveFileException;
import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.application.services.IManageFilesService;
import it.unitn.disi.util.io.FileIOUtil;
import it.unitn.disi.util.logging.LogUtil;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * TODO: document this type.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public abstract class ManageFilesService implements IManageFilesService {
	/**
	 * TODO: document this method.
	 * 
	 * @param progressMonitor
	 * @param file
	 * @return
	 * @throws CouldNotCreateFileException
	 */
	protected IFile createNewFile(IProgressMonitor progressMonitor, IFile file) throws CouldNotCreateFileException {
		String projectName = file.getProject().getName();
		String filePath = file.getFullPath().toString();
		LogUtil.log.info("Creating new file {0} in project: {1}.", filePath, projectName); //$NON-NLS-1$

		// Creates the new file in the project.
		try {
			file.create(null, true, progressMonitor);
		}
		catch (CoreException e) {
			LogUtil.log.error("Unagi caught an Eclipse exception while trying to create a file {0} in project {1}.", e, filePath, projectName); //$NON-NLS-1$
			throw new CouldNotCreateFileException(file, e);
		}

		// Returns the newly created file.
		return file;
	}

	/**
	 * TODO: document this method.
	 * 
	 * @param folder
	 * @param name
	 * @param extension
	 * @return
	 * @throws UnagiException 
	 */
	protected IFile generateCreatableFile(IFolder folder, String fileName) throws CouldNotCreateFileException {
		// Creates an IFile reference to it in the specified folder.
		IFile file = folder.getFile(fileName);

		// Checks that the file can be created.
		checkCreatableFile(file);

		// If passed the check, returns the file.
		LogUtil.log.debug("Returning file object after checking it can be created: {0}.", file.getFullPath()); //$NON-NLS-1$
		return file;
	}

	/**
	 * TODO: document this method.
	 * 
	 * @param file
	 * @throws UnagiException
	 */
	protected void checkCreatableFile(IFile file) throws CouldNotCreateFileException {
		IProject project = file.getProject();
		String projectName = project.getName();
		String filePath = file.getFullPath().toString();

		// Checks if the project really exists.
		if (!project.exists()) {
			LogUtil.log.error("Cannot create new file {0}, project doesn't exist: {1}.", filePath, projectName); //$NON-NLS-1$
			throw new CouldNotCreateFileException(file);
		}

		// Checks that the folder in which the file should be created exists and is accessible.
		IFolder folder = project.getFolder(file.getFullPath().removeLastSegments(1));
		if ((!folder.exists()) || (!folder.isAccessible())) {
			LogUtil.log.error("Cannot create new file {0} in project {1}. Containing folder doesn't exist or is not accessible.", filePath, projectName); //$NON-NLS-1$
			throw new CouldNotCreateFileException(file);
		}

		// Checks that a file in this path doesn't yet exist.
		if (file.exists()) {
			LogUtil.log.error("Cannot create new file {0} in project {1}. The file already exists.", filePath, projectName); //$NON-NLS-1$
			throw new CouldNotCreateFileException(file);
		}
	}

	/** @see it.unitn.disi.unagi.application.services.IManageFilesService#readFile(org.eclipse.core.resources.IFile) */
	@Override
	public StringBuffer readFile(IFile file) throws CouldNotReadFileException {
		// Tries to read the contents of the file from its location.
		try {
			return FileIOUtil.readFile(file.getLocation().toString());
		}

		// In case of I/O errors, throws an application exception.
		catch (IOException e) {
			throw new CouldNotReadFileException(file, e);
		}
	}

	/**
	 * @see it.unitn.disi.unagi.application.services.IManageFilesService#saveFile(org.eclipse.core.resources.IFile,
	 *      java.lang.String)
	 */
	@Override
	public void saveFile(IFile file, String contents) throws CouldNotSaveFileException {
		// Tries to save the contents to the file at its location.
		try {
			FileIOUtil.saveFile(file.getLocation().toString(), contents);
		}

		// In case of I/O errors, throws an application exception.
		catch (IOException e) {
			throw new CouldNotSaveFileException(file, e);
		}
	}

	/**
	 * TODO: document this method.
	 * 
	 * @param progressMonitor
	 * @param file
	 * @throws CouldNotDeleteFileException
	 */
	protected void deleteFile(IProgressMonitor progressMonitor, IFile file) throws CouldNotDeleteFileException {
		String projectName = file.getProject().getName();
		String filePath = file.getFullPath().toString();

		// Deletes the file from the workspace.
		try {
			file.delete(true, progressMonitor);
		}
		catch (CoreException e) {
			LogUtil.log.error("Unagi caught an Eclipse exception while trying to delete file {0} in project {1}.", e, filePath, projectName); //$NON-NLS-1$
			throw new CouldNotDeleteFileException(file, e);
		}
	}
}
