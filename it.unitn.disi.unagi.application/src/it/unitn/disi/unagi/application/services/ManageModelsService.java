package it.unitn.disi.unagi.application.services;

import it.unitn.disi.unagi.application.exceptions.CouldNotCreateModelsSubdirectoryException;
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
	 * @return
	 * @throws CouldNotCreateModelsSubdirectoryException
	 */
	RequirementsModel createNewRequirementsModel(UnagiProject project, String name) throws CouldNotCreateModelsSubdirectoryException;
}
