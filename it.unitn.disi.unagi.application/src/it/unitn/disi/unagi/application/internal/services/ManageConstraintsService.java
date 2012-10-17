package it.unitn.disi.unagi.application.internal.services;

import it.unitn.disi.unagi.application.Activator;
import it.unitn.disi.unagi.application.exceptions.CouldNotCreateFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotDeleteFileException;
import it.unitn.disi.unagi.application.services.IManageConstraintsService;
import it.unitn.disi.unagi.application.services.IManageProjectsService;
import it.unitn.disi.util.io.FileIOUtil;
import it.unitn.disi.util.logging.LogUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * TODO: document this type.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class ManageConstraintsService extends ManageFilesService implements IManageConstraintsService {
	/**
	 * @see it.unitn.disi.unagi.application.services.IManageConstraintsService#createNewConstraintsFile(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.resources.IProject, java.lang.String)
	 */
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

		// Generates initial contents for the constraints file.
		try {
			createConstraintsContents(progressMonitor, constraintsFile, name);
		}
		catch (IOException | CoreException e) {
			LogUtil.log.error("Could not create initial contents for requirements model {0}.", e, constraintsFile.getFullPath()); //$NON-NLS-1$
			throw new CouldNotCreateFileException(constraintsFile);
		}

		// Returns the newly created file.
		return constraintsFile;
	}

	/**
	 * TODO: document this method.
	 * 
	 * @param constraintsFile
	 * @param fileName
	 * @throws CoreException
	 * @throws IOException
	 */
	private void createConstraintsContents(IProgressMonitor progressMonitor, IFile constraintsFile, String fileName) throws CoreException, IOException {
		// Loads the template for constraints files from the plug-in bundle.
		URL templateFileURL = FileLocator.find(Activator.getBundle(), CONSTRAINTS_TEMPLATE_FILE_PATH, Collections.EMPTY_MAP);

		// Process the template, replacing the package name with the name of the file (without extension).
		Map<String, Object> map = new HashMap<>();
		map.put(CONSTRAINTS_VARIABLE_PACKAGE_NAME, fileName);
		String contents = FileIOUtil.processTemplate(templateFileURL, map);

		// Changes the contents of the constraints file.
		InputStream source = new ByteArrayInputStream(contents.getBytes());
		constraintsFile.setContents(source, true, false, progressMonitor);
	}

	/**
	 * @see it.unitn.disi.unagi.application.services.IManageConstraintsService#deleteConstraintsFile(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.resources.IFile)
	 */
	@Override
	public void deleteConstraintsFile(IProgressMonitor progressMonitor, IFile constraintsFile) throws CouldNotDeleteFileException {
		// Deletes the file from the workspace.
		deleteFile(progressMonitor, constraintsFile);
	}
}
