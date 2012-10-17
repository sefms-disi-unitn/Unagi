package it.unitn.disi.unagi.application.internal.services;

import it.unitn.disi.unagi.application.exceptions.CouldNotCreateFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotDeleteFileException;
import it.unitn.disi.unagi.application.services.IManageConstraintsService;
import it.unitn.disi.unagi.application.services.IManageProjectsService;
import it.unitn.disi.util.logging.LogUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * TODO: document this type.
 *
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class ManageConstraintsService extends ManageFilesService implements IManageConstraintsService {
	/** @see it.unitn.disi.unagi.application.services.IManageConstraintsService#createNewConstraintsFile(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.core.resources.IProject, java.lang.String) */
	@Override
	public IFile createNewConstraintsFile(IProgressMonitor progressMonitor, IProject project, String name) throws CouldNotCreateFileException {
		String projectName = project.getName();
		LogUtil.log.info("Creating a new constraints file {0} in project {1}.", name, projectName); //$NON-NLS-1$

		// Obtains the models folder.
		IFolder modelsFolder = project.getFolder(IManageProjectsService.MODELS_PROJECT_SUBDIR);

		// Generates a file object in the folder, checking that the file can be created later.
		String fileName = name + '.' + CONSTRAINTS_FILE_EXTENSION;
		IFile constraintsFile = generateCreatableFile(modelsFolder, fileName);

		// Creates the new constraints file in the project.
		createNewFile(progressMonitor, constraintsFile);

		// Returns the newly created file.
		return constraintsFile;
	}

	/** @see it.unitn.disi.unagi.application.services.IManageConstraintsService#deleteConstraintsFile(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.core.resources.IFile) */
	@Override
	public void deleteConstraintsFile(IProgressMonitor progressMonitor, IFile constraintsFile) throws CouldNotDeleteFileException {
		// Deletes the file from the workspace.
		deleteFile(progressMonitor, constraintsFile);
	}
}
