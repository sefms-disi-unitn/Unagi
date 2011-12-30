package it.unitn.disi.unagi.application.services;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.domain.core.RequirementsModel;
import it.unitn.disi.unagi.domain.core.UnagiProject;

/**
 * Interface for the service "Manage Models", which allows the user to manipulate (create, delete, etc.) models in a
 * Unagi project.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public interface ManageModelsService {
	/**
	 * Creates a new requirements model in the given Unagi project.
	 * 
	 * @param project
	 *          The Unagi project that will hold the requirements model.
	 * @param name
	 *          The name of the requirements model.
	 * @param basePackage
	 *          The base package for classes generated from this requirements model.
	 * @return A RequirementsModel instance, which is a domain class that represents requirements models.
	 * @throws UnagiException
	 *           If, for some reason: (a) the "models" sub-directory did not exist and could not be created in the Unagi
	 *           project's directory; (b) the file that represents the requirements model could not be created in the
	 *           "models" sub-directory of the Unagi project's directory; or (c) the new project could not be saved.
	 */
	RequirementsModel createNewRequirementsModel(UnagiProject project, String name, String basePackage) throws UnagiException;

	/**
	 * Deletes the specified requirements model, removing it from its parent Unagi project.
	 * 
	 * @param model
	 * @throws UnagiException
	 *           If, for some reason: (a) the file that represents the requirements model in the "models" sub-directory of
	 *           the Unagi project's directory cannot be deleted; or (b) the new project could not be saved.
	 */
	void deleteRequirementsModel(RequirementsModel model) throws UnagiException;

	/**
	 * TODO: document this method.
	 * 
	 * @param model
	 * @throws UnagiException
	 */
	void compileRequirementsModel(RequirementsModel model) throws UnagiException;
}
