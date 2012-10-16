package it.unitn.disi.unagi.rcpapp.views;

import it.unitn.disi.unagi.application.services.IManageFilesService;
import it.unitn.disi.unagi.application.services.IManageSourcesService;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;

/**
 * TODO: document this type.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
@Creatable
public class JavaSourceEditorPart extends AbstractTextEditorPart {
	/** ID of this part. */
	public static final String PART_ID = JavaSourceEditorPart.class.getName();

	/** Service class for model management. */
	@Inject
	private IManageSourcesService manageSourcesService;

	/** @see it.unitn.disi.unagi.rcpapp.views.AbstractTextEditorPart#getManageFilesService() */
	@Override
	protected IManageFilesService getManageFilesService() {
		return manageSourcesService;
	}
}
