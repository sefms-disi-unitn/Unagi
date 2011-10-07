package it.unitn.disi.unagi.application.services;

import it.unitn.disi.unagi.application.exceptions.CouldNotCreateModelsSubdirectoryException;
import it.unitn.disi.unagi.application.exceptions.CouldNotCreateRequirementsModelFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotDeleteRequirementsModelFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotSaveUnagiProjectException;
import it.unitn.disi.unagi.domain.core.RequirementsModel;
import it.unitn.disi.unagi.domain.core.UnagiProject;

/**
 * TODO: document this type.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public interface ManageModelsService {
	/**
	 * TODO: document this method.
	 * 
	 * @param project
	 * @param name
	 * @param basePackage
	 * @return
	 * @throws CouldNotCreateModelsSubdirectoryException
	 * @throws CouldNotCreateRequirementsModelFileException
	 * @throws CouldNotSaveUnagiProjectException
	 */
	RequirementsModel createNewRequirementsModel(UnagiProject project, String name, String basePackage) throws CouldNotCreateModelsSubdirectoryException, CouldNotCreateRequirementsModelFileException, CouldNotSaveUnagiProjectException;

	/**
	 * TODO: document this method.
	 * 
	 * @param model
	 * @throws CouldNotDeleteRequirementsModelFileException
	 * @throws CouldNotSaveUnagiProjectException
	 */
	void deleteRequirementsModel(RequirementsModel model) throws CouldNotDeleteRequirementsModelFileException, CouldNotSaveUnagiProjectException;
}
