package it.unitn.disi.unagi.rcpapp.handlers;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.application.services.IManageFilesService;
import it.unitn.disi.unagi.rcpapp.IUnagiRcpAppBundleInfoProvider;
import it.unitn.disi.unagi.rcpapp.views.RequirementsModelEditorPart;
import it.unitn.disi.util.logging.LogUtil;

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MInputPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.swt.widgets.Display;

/**
 * Handler for the "Open Requirements Model(s)" command.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class OpenRequirementsModelsHandler extends AbstractRequirementsModelsHandler {
	/** The bundle's activator, used to retrieve global information about the bundle. */
	@Inject
	private IUnagiRcpAppBundleInfoProvider activator;

	/** Model object that represents the entire RCP application. */
	@Inject
	private MApplication application;

	/** The platform's model service, used to retrieve parts from the application's E4 model. */
	@Inject
	private EModelService modelService;

	/** The platform's parts service, used to show and activate parts in the workbench. */
	@Inject
	private EPartService partService;

	/**
	 * Method that handles the command's execution.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 */
	@Execute
	public void execute(ESelectionService selectionService) {
		LogUtil.log.debug("Executing \"Open Requirements Model(s)\" command."); //$NON-NLS-1$

		// This command can be executed for multiple models.
		executeForMultipleModels(selectionService);
	}

	/**
	 * Method that informs the workbench if the command can be executed.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return <code>true</code> if the current selection allows the execution of the command, <code>false</code>
	 *         otherwise.
	 */
	@CanExecute
	public boolean canExecute(ESelectionService selectionService) {
		// Can only compile requirements models if at least one of them is selected.
		return isAtLeastOneModelSelected(selectionService);
	}

	/**
	 * @see it.unitn.disi.unagi.rcpapp.handlers.AbstractRequirementsModelsHandler#doExecute(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.resources.IFile)
	 */
	@Override
	protected void doExecute(IProgressMonitor monitor, final IFile model) throws UnagiException {
		final String modelName = model.getName();
		final String modelURI = model.getLocation().toString();

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				// Retrieves the part stack in which the model editor will be open.
				MPartStack stack = (MPartStack) modelService.find(activator.getEditorStackId(), application);

				// Creates a new part for the model editor, sets its label and indicates the Requirements Model Editor as the part to use.
				String editorURI = "bundleclass://" + activator.getBundleId() + '/' + RequirementsModelEditorPart.PART_ID; //$NON-NLS-1$
				MInputPart part = MBasicFactory.INSTANCE.createInputPart();
				part.setLabel(modelName);
				part.setContributionURI(editorURI);
				part.setCloseable(true);

				// Sets the requirements model file URI as the input for the editor.
				part.setInputURI(modelURI.toString());
				
				// Embeds extra information in the transient data map, such as the IFile object that is being opened.
				part.getTransientData().put(IManageFilesService.FILE_KEY, model);

				// Opens and activates the model editor.
				stack.getChildren().add(part);
				partService.showPart(part, PartState.ACTIVATE);
			}
		});
	}
}
