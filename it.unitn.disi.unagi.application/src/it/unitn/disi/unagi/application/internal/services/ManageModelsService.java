package it.unitn.disi.unagi.application.internal.services;

import it.unitn.disi.unagi.application.Activator;
import it.unitn.disi.unagi.application.exceptions.CouldNotDeleteRequirementsModelException;
import it.unitn.disi.unagi.application.exceptions.CouldNotSaveRequirementsModelException;
import it.unitn.disi.unagi.application.services.IManageModelsService;
import it.unitn.disi.unagi.application.services.IManageProjectsService;
import it.unitn.disi.util.logging.LogUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

/**
 * Implementation of the service class for model management.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class ManageModelsService implements IManageModelsService {
	/**
	 * @see it.unitn.disi.unagi.application.services.IManageModelsService#createNewRequirementsModel(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.resources.IProject, java.lang.String, java.lang.String)
	 */
	@Override
	public IFile createNewRequirementsModel(IProgressMonitor progressMonitor, IProject project, String name, String basePackage) throws CouldNotSaveRequirementsModelException {
		String projectName = project.getName();
		LogUtil.log.info("Creating a new requirements model in project: {0}.", projectName); //$NON-NLS-1$

		// If the project doesn't exist, throws a Unagi exception.
		if (!project.exists()) {
			LogUtil.log.warn("Cannot create new requirements model, project already exists: {0}.", projectName); //$NON-NLS-1$
			throw new CouldNotSaveRequirementsModelException(project);
		}

		// Obtains the models folder. Checks if it exists and is accessible.
		IFolder modelsFolder = project.getFolder(IManageProjectsService.MODELS_PROJECT_SUBDIR);
		if ((!modelsFolder.exists()) || (!modelsFolder.isAccessible())) {
			LogUtil.log.warn("Cannot create new requirements model, project's models folder doesn't exist or is not accessible: {0}.", projectName); //$NON-NLS-1$
			throw new CouldNotSaveRequirementsModelException(project);
		}

		// Determines the name of the file to be created, generating new names if a file with the same name already exists.
		int count = 2;
		String fileName = name + "." + IManageModelsService.REQUIREMENTS_MODEL_EXTENSION; //$NON-NLS-1$
		IFile modelFile = modelsFolder.getFile(fileName);
		while (modelFile.exists()) {
			fileName = name + (count++) + "." + IManageModelsService.REQUIREMENTS_MODEL_EXTENSION; //$NON-NLS-1$
			modelFile = modelsFolder.getFile(fileName);
		}

		// Creates the new file in the project.
		try {
			modelFile.create(null, true, progressMonitor);
			createRequirementsEMFContents(modelFile, name, basePackage);
		}
		catch (CoreException | IOException e) {
			LogUtil.log.error("Unagi caught an Eclipse exception while trying to create a new requirements model in project: {0}.", e, projectName); //$NON-NLS-1$
			throw new CouldNotSaveRequirementsModelException(project, e);
		}

		// Returns the newly created file.
		return modelFile;
	}

	/**
	 * @see it.unitn.disi.unagi.application.services.IManageModelsService#deleteRequirementsModel(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.resources.IFile)
	 */
	@Override
	public void deleteRequirementsModel(IProgressMonitor progressMonitor, IFile modelFile) throws CouldNotDeleteRequirementsModelException {
		// Deletes the file from the workspace.
		try {
			modelFile.delete(true, progressMonitor);
		}
		catch (CoreException e) {
			LogUtil.log.error("Unagi caught an Eclipse exception while trying to delete a requirements model: {0}.", e, modelFile.getName()); //$NON-NLS-1$
			throw new CouldNotDeleteRequirementsModelException(modelFile, e);
		}
	}

	/**
	 * Internal method that creates the basic EMF contents of a new ECore file that is being created.
	 * 
	 * @param modelFile
	 *          The workspace file in which to put the contents.
	 * @throws IOException
	 *           In case any I/O errors occur during this process.
	 */
	private void createRequirementsEMFContents(IFile modelFile, String name, String basePackage) throws IOException {
		String modelFilePath = modelFile.getLocationURI().toString();
		LogUtil.log.info("Creating basic EMF contents for requirements file: {0}.", modelFilePath); //$NON-NLS-1$

		// Initializes the standalone factory implementation for ecore files and a new resource set.
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(IManageModelsService.REQUIREMENTS_MODEL_EXTENSION, new EcoreResourceFactoryImpl());
		ResourceSet resourceSet = new ResourceSetImpl();

		// Loads the goal model EMF file.
		URL url = FileLocator.find(Activator.getBundle(), IManageModelsService.GOALMODEL_EMF_FILE_PATH, Collections.EMPTY_MAP);
		Resource ddlResource = resourceSet.createResource(URI.createURI(url.toString()));
		ddlResource.load(Collections.EMPTY_MAP);

		// Load the package from the goal model EMF file and create a new package for the requirements file.
		final EPackage ddlPackage = (EPackage) ddlResource.getContents().get(0);
		final EPackage newPackage = EcoreFactory.eINSTANCE.createEPackage();
		newPackage.setName(name);
		newPackage.setNsPrefix(basePackage);
		newPackage.setNsURI(modelFile.getName());

		// Create the main goal for the requirements file and adds it to its package.
		final EClass mainGoal = EcoreFactory.eINSTANCE.createEClass();
		mainGoal.setName(IManageModelsService.MAIN_GOAL_BASE_NAME);
		newPackage.getEClassifiers().add(mainGoal);

		// Add the class Goal from the Goal Model EMF file as superclass of the main goal in the requirements model.
		final EClass superClass = (EClass) ddlPackage.getEClassifier("Goal"); //$NON-NLS-1$
		mainGoal.getESuperTypes().add(superClass);

		// Finally, create a new resource to save the requirements package into a new model file.
		Resource outputRes = resourceSet.createResource(URI.createURI(modelFilePath));
		outputRes.getContents().add(newPackage);
		outputRes.save(Collections.EMPTY_MAP);
	}
}
