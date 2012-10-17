package it.unitn.disi.unagi.application.internal.services;

import it.unitn.disi.unagi.application.Activator;
import it.unitn.disi.unagi.application.exceptions.CouldNotCreateFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotDeleteFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotGenerateRequirementsClassesException;
import it.unitn.disi.unagi.application.nls.Messages;
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
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.codegen.ecore.generator.Generator;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelFactory;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenBaseGeneratorAdapter;
import org.eclipse.emf.codegen.util.CodeGenUtil;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
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
public class ManageModelsService extends ManageFilesService implements IManageModelsService {
	/** TODO: document this field. */
	private static final String GENMODEL_FILE_EXTENSION = "genmodel"; //$NON-NLS-1$

	/**
	 * @see it.unitn.disi.unagi.application.services.IManageModelsService#createNewRequirementsModel(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.resources.IProject, java.lang.String, java.lang.String)
	 */
	@Override
	public IFile createNewRequirementsModel(IProgressMonitor progressMonitor, IProject project, String name, String basePackage) throws CouldNotCreateFileException {
		String projectName = project.getName();
		LogUtil.log.info("Creating new requirements model {0} (base package: {1}) in project {2}.", name, basePackage, projectName); //$NON-NLS-1$

		// Obtains the models folder.
		IFolder modelsFolder = project.getFolder(IManageProjectsService.MODELS_PROJECT_SUBDIR);

		// Generates a file object in the folder, checking that the file can be created later.
		String fileName = name + '.' + REQUIREMENTS_MODEL_EXTENSION;
		IFile modelFile = generateCreatableFile(modelsFolder, fileName);

		// Creates the new model file in the project.
		createNewFile(progressMonitor, modelFile);

		// Generates initial contents for the requirements file.
		try {
			createRequirementsContents(modelFile, name, basePackage);
		}
		catch (IOException e) {
			LogUtil.log.error("Could not create initial contents for requirements model {0} in project {1}.", e, modelFile.getFullPath(), projectName); //$NON-NLS-1$
			throw new CouldNotCreateFileException(modelFile);
		}

		// Returns the newly created file.
		return modelFile;
	}

	/**
	 * Internal method that creates the basic EMF contents of a new ECore file that is being created.
	 * 
	 * @param modelFile
	 *          The workspace file in which to put the contents.
	 * @throws IOException
	 *           In case any I/O errors occur during this process.
	 */
	private void createRequirementsContents(IFile modelFile, String name, String basePackage) throws IOException {
		String modelFilePath = modelFile.getLocationURI().toString();
		LogUtil.log.info("Creating basic EMF contents for requirements file: {0}.", modelFilePath); //$NON-NLS-1$

		// Initializes the standalone factory implementation for ecore files and a new resource set.
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(IManageModelsService.REQUIREMENTS_MODEL_EXTENSION, new EcoreResourceFactoryImpl());
		ResourceSet resourceSet = new ResourceSetImpl();

		// Loads the goal model EMF file.
		URL url = FileLocator.find(Activator.getBundle(), IManageModelsService.GORE_EMF_FILE_PATH, Collections.EMPTY_MAP);
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

	/**
	 * @see it.unitn.disi.unagi.application.services.IManageModelsService#deleteRequirementsModel(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.resources.IFile)
	 */
	@Override
	public void deleteRequirementsModel(IProgressMonitor progressMonitor, IFile modelFile) throws CouldNotDeleteFileException {
		// Deletes the file from the workspace.
		deleteFile(progressMonitor, modelFile);
	}

	/**
	 * @see it.unitn.disi.unagi.application.services.IManageModelsService#generateRequirementsClasses(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.resources.IFile)
	 */
	@Override
	public IFolder generateRequirementsClasses(IProgressMonitor progressMonitor, IFile modelFile) throws CouldNotGenerateRequirementsClassesException {
		try {
			IProject project = modelFile.getProject();
			IFolder sourcesFolder = project.getFolder(IManageProjectsService.SOURCES_PROJECT_SUBDIR);

			// Extracts model name and base package from the model file.
			EPackage ePackage = extractEMFPackage(modelFile);
			String modelName = modelFile.getLocation().removeFileExtension().lastSegment();
			String basePackage = ePackage.getNsPrefix();

			// Creates the genmodel file that is used to generate the Java classes.
			Resource genModelResource = createGenModelFile(progressMonitor, modelFile, modelName, basePackage, sourcesFolder);

			// Generates the sources for the classes declared in the model.
			generateClasses(progressMonitor, genModelResource, modelFile);

			return sourcesFolder;
		}
		catch (IOException | CoreException e) {
			LogUtil.log.error("Unagi caught an exception while trying to generate sources from a requirements model: {0}.", e, modelFile.getName()); //$NON-NLS-1$
			throw new CouldNotGenerateRequirementsClassesException(modelFile, e);
		}
	}

	/**
	 * TODO: document this method.
	 * 
	 * @param modelFile
	 * @return
	 */
	private EPackage extractEMFPackage(IFile modelFile) {
		// Initializes the ECore model.
		EcorePackage.eINSTANCE.eClass();

		// Loads the EMF file as a resource.
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.getResource(URI.createPlatformResourceURI(modelFile.getFullPath().toString(), true), true);

		// Retrieves the package object.
		EPackage ePackage = (EPackage) resource.getContents().get(0);
		return ePackage;
	}

	/**
	 * TODO: document this method.
	 * 
	 * This method's implementation is based on code taken from the run() method of class
	 * org.eclipse.emf.codegen.ecore.Generator.
	 * 
	 * @param modelName
	 * @param modelFile
	 * @param basePackage
	 * @param sourcesFolder
	 * @return
	 * @throws CouldNotGenerateRequirementsClassesException
	 * @see org.eclipse.emf.codegen.ecore.Generator
	 */
	private Resource createGenModelFile(IProgressMonitor progressMonitor, IFile modelFile, String modelName, String basePackage, IFolder sourcesFolder) throws IOException {
		// Create paths and URI objects for the model, the sources directory and the genmodel file.
		IPath ecorePath = modelFile.getLocation();
		IPath sourcesDirPath = sourcesFolder.getFullPath();
		IPath genModelPath = ecorePath.removeFileExtension().addFileExtension(GENMODEL_FILE_EXTENSION);
		URI ecoreURI = URI.createFileURI(ecorePath.toString());
		URI genModelURI = URI.createFileURI(genModelPath.toString());

		// Obtains the ECore package from the model file.
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getURIConverter().getURIMap().putAll(EcorePlugin.computePlatformURIMap());
		Resource resource = resourceSet.getResource(ecoreURI, true);
		EPackage ePackage = (EPackage) resource.getContents().get(0);

		// Updates the progress monitor with the task that is about to happen.
		progressMonitor.beginTask("", 2); //$NON-NLS-1$
		progressMonitor.subTask(Messages.getFormattedString("task.generateRequirementsClasses.createGenModelFile.description", modelName, genModelPath)); //$NON-NLS-1$

		// Creates the genmodel file as a resource in the workspace and configures its parameters.
		// setModelDirectory() indicates where the class generation should take place.
		Resource genModelResource = Resource.Factory.Registry.INSTANCE.getFactory(genModelURI).createResource(genModelURI);
		GenModel genModel = GenModelFactory.eINSTANCE.createGenModel();
		genModelResource.getContents().add(genModel);
		resourceSet.getResources().add(genModelResource);
		genModel.setModelDirectory(sourcesDirPath.toString());
		genModel.getForeignModel().add(ecorePath.lastSegment());
		genModel.initialize(Collections.singleton(ePackage));
		GenPackage genPackage = genModel.getGenPackages().get(0);
		genModel.setModelName(genModelURI.trimFileExtension().lastSegment());
		genPackage.setBasePackage(basePackage);

		//
		URL baseGenModelURL = FileLocator.find(Activator.getBundle(), IManageModelsService.BASE_GENMODEL_FILE_PATH, Collections.EMPTY_MAP);
		Resource baseGenModelResource = resourceSet.createResource(URI.createURI(baseGenModelURL.toString()));
		baseGenModelResource.load(Collections.EMPTY_MAP);
		GenModel baseGenModel = (GenModel) baseGenModelResource.getContents().get(0);
		for (GenPackage pkg : baseGenModel.getGenPackages())
			genModel.getUsedGenPackages().add(pkg);

		// Generates the genmodel file and updates the progress monitor.
		progressMonitor.worked(1);
		genModelResource.save(Collections.EMPTY_MAP);

		// Returns the genmodel object.
		return genModelResource;
	}

	private void generateClasses(IProgressMonitor progressMonitor, Resource genModelResource, IFile modelFile) throws CoreException {
		// Retrieves the genmodel object from the genmodel resource and checks that it's valid.
		GenModel genModel = (GenModel) genModelResource.getContents().get(0);
		IStatus status = genModel.validate();
		if (!status.isOK()) {
			Diagnostic diagnostic = genModel.diagnose();
			throw new IllegalStateException("Genmodel file is not valid. Diagnostic message is: " + diagnostic.getMessage()); //$NON-NLS-1$
		}

		// Creates a generator from the genmodel file and configures the generation.
		Generator generator = new Generator();
		generator.setInput(genModel);
		genModel.setForceOverwrite(true);
		genModel.reconcile();
		genModel.setCanGenerate(true);
		genModel.setValidateModel(true);
		genModel.setUpdateClasspath(false);
		genModel.setCodeFormatting(true);

		// Generates the source code following the instructions contained in the genmodel file.
		generator.generate(genModel, GenBaseGeneratorAdapter.MODEL_PROJECT_TYPE, CodeGenUtil.EclipseUtil.createMonitor(progressMonitor, 1));
	}
}
