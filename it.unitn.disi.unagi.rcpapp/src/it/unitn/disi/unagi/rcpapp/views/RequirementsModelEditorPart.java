package it.unitn.disi.unagi.rcpapp.views;

import it.unitn.disi.unagi.application.services.IManageFilesService;
import it.unitn.disi.unagi.application.services.IManageModelsService;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;

/**
 * Editor part for requirements models. Should be loaded in the "editor area" when the user open one of the models she
 * has created.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
@Creatable
public class RequirementsModelEditorPart extends AbstractTextEditorPart {
	/** ID of this part. */
	public static final String PART_ID = RequirementsModelEditorPart.class.getName();

	/** Service class for model management. */
	@Inject
	private IManageModelsService manageModelsService;

	/** @see it.unitn.disi.unagi.rcpapp.views.AbstractTextEditorPart#getManageFilesService() */
	@Override
	protected IManageFilesService getManageFilesService() {
		return manageModelsService;
	}
}
