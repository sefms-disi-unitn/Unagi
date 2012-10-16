package it.unitn.disi.unagi.rcpapp.views;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.application.services.IManageFilesService;
import it.unitn.disi.unagi.rcpapp.IUnagiRcpAppBundleInfoProvider;
import it.unitn.disi.unagi.rcpapp.nls.Messages;
import it.unitn.disi.util.gui.DialogUtil;
import it.unitn.disi.util.logging.LogUtil;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.MInput;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * TODO: document this type.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
@Creatable
public abstract class AbstractTextEditorPart implements MDirtyable, ModifyListener {
	/** The bundle's activator, used to retrieve global information about the bundle. */
	@Inject
	protected IUnagiRcpAppBundleInfoProvider activator;

	/** The string key that corresponds to the concrete class that implements this abstract class. */
	private String classKey;

	/** TODO: document this field. */
	protected MPart part;
	
	/** TODO: document this field. */
	protected IFile file;

	/** TODO: document this field. */
	protected Text textWidget;

	/**
	 * TODO: document this method.
	 * 
	 * @param parent
	 */
	@PostConstruct
	public void init(Composite parent, MInput input, MPart part) {
		this.part = part;
		
		// Retrieves the reference to the IFile object from the transient map.
		file = (IFile)part.getTransientData().get(IManageFilesService.FILE_KEY);

		// Uses a single text widget for the entire editor.
		parent.setLayout(new FillLayout());
		textWidget = new Text(parent, SWT.MULTI | SWT.WRAP);

		// Retrieves the contents of the file that is being edited.
		textWidget.setText(retrieveFileContents());

		// Sets itself as a listener for changes in the text widget.
		textWidget.addModifyListener(this);
	}

	/**
	 * TODO: document this method.
	 */
	@Focus
	protected void setFocus() {
		textWidget.setFocus();
	}

	/** @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent) */
	@Override
	public void modifyText(ModifyEvent e) {
		String label = part.getLabel();
		LogUtil.log.debug("Changes detected in the editor for {0}. Marking it as dirty...", label); //$NON-NLS-1$

		// When there's a change, marks the editor as dirty.
		setDirty(true);
	}

	/** @see org.eclipse.e4.ui.model.application.ui.MDirtyable#isDirty() */
	@Override
	public boolean isDirty() {
		return part.isDirty();
	}

	/** @see org.eclipse.e4.ui.model.application.ui.MDirtyable#setDirty(boolean) */
	@Override
	public void setDirty(boolean dirty) {
		String label = part.getLabel();

		// Checks if the editor was clean and is now marked as dirty.
		if (!part.isDirty() && dirty) {

			// Then removes itself as a listener until the file is saved to avoid unnecessary event notifications.
			textWidget.removeModifyListener(this);

			// Also, changes the label of the editor to include a star, if not already there.
			if (label.charAt(0) != '*')
				part.setLabel('*' + label);
		}

		// Checks if the editor was dirty and is now marked as clean.
		else if (!dirty && part.isDirty()) {
			// Changes the label of the editor to remove the star, if it was there.
			if (label.charAt(0) == '*')
				part.setLabel(label.substring(1));

			// Then places the editor as listener for changes in the text widget again.
			textWidget.addModifyListener(this);
		}

		// Effectively changes the dirty state of the editor.
		part.setDirty(dirty);
	}

	/**
	 * Saves the contents of the editor back to its original file.
	 */
	@Persist
	public void save() {
		String path = file.getFullPath().toString();
		
		// Asks the service class to save the file.
		try {
			LogUtil.log.info("Saving the contents of editor {0} to file: {1}", part.getLabel(), path); //$NON-NLS-1$
			IManageFilesService manageFilesService = getManageFilesService();
			manageFilesService.saveFile(file, textWidget.getText());

			// If writing was successful (no exceptions), sets the editor as clean.
			setDirty(false);
		}
		
		// In case of application errors, shows an error dialog.
		catch (UnagiException e) {
			String classKey = getClassKey();
			LogUtil.log.error("Unable to read file: {0}.", e, path); //$NON-NLS-1$
			final String statusMsg = Messages.getString("gui." + classKey + ".error.write.status"); //$NON-NLS-1$ //$NON-NLS-2$
			final String errorTitle = Messages.getString("gui." + classKey + ".error.write.title"); //$NON-NLS-1$ //$NON-NLS-2$
			final String errorMessage = Messages.getFormattedString("gui." + classKey + ".error.write.message", path); //$NON-NLS-1$ //$NON-NLS-2$
			DialogUtil.displayError(activator.getBundleId(), statusMsg, errorTitle, errorMessage);
		}
	}

	/**
	 * Retrieves the contents of the file referred to by the given URI and returns it.
	 * 
	 * @return The contents of the file referred to by the given URI.
	 */
	private String retrieveFileContents() {
		String path = file.getFullPath().toString();
		
		try {
			LogUtil.log.info("Reading the contents of file {0} for editor: {1}", path, part.getLabel()); //$NON-NLS-1$
			IManageFilesService manageFilesService = getManageFilesService();
			StringBuffer buffer = manageFilesService.readFile(file);
			return buffer.toString();
		}

		// In case of application errors, shows an error dialog.
		catch (UnagiException e) {
			String classKey = getClassKey();
			LogUtil.log.error("Unable to read file from URI: {0}.", e, path); //$NON-NLS-1$
			final String statusMsg = Messages.getString("gui." + classKey + ".error.read.status"); //$NON-NLS-1$ //$NON-NLS-2$
			final String errorTitle = Messages.getString("gui." + classKey + ".error.read.title"); //$NON-NLS-1$ //$NON-NLS-2$
			final String errorMessage = Messages.getFormattedString("gui." + classKey + ".error.read.message", path); //$NON-NLS-1$ //$NON-NLS-2$
			DialogUtil.displayError(activator.getBundleId(), statusMsg, errorTitle, errorMessage);
			return null;
		}
	}

	/**
	 * Builds and returns the string key that corresponds to the concrete class that implements this abstract class.
	 * 
	 * @return The string key that corresponds to the concrete part.
	 */
	protected String getClassKey() {
		if (classKey == null) {
			String className = getClass().getSimpleName();
			classKey = className.substring(0, 1).toLowerCase() + className.substring(1);
		}

		return classKey;
	}

	/**
	 * TODO: document this method.
	 * 
	 * @return
	 */
	protected abstract IManageFilesService getManageFilesService();
}
