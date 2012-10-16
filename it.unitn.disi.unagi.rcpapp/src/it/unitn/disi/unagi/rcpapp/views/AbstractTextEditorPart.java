package it.unitn.disi.unagi.rcpapp.views;

import it.unitn.disi.unagi.rcpapp.IUnagiRcpAppBundleInfoProvider;
import it.unitn.disi.unagi.rcpapp.nls.Messages;
import it.unitn.disi.util.gui.DialogUtil;
import it.unitn.disi.util.logging.LogUtil;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

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
	protected MInput input;
	
	/** TODO: document this field. */
	protected MPart part;

	/** TODO: document this field. */
	protected Text textWidget;

	/**
	 * TODO: document this method.
	 * 
	 * @param parent
	 */
	@PostConstruct
	public void init(Composite parent, MInput input, MPart part) {
		this.input = input;
		this.part = part;

		// Uses a single text widget for the entire editor.
		parent.setLayout(new FillLayout());
		textWidget = new Text(parent, SWT.MULTI | SWT.WRAP);

		// Retrieves the contents of the file that is being edited.
		textWidget.setText(retrieveFileContents(input.getInputURI()));

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
		// FIXME: should this be in the application layer? Check other cases in which this is also true in this project...
		FileWriter writer = null;
		String uri = input.getInputURI();
		
		// Creates a string reader to read the contents of the editor.
		StringReader reader = new StringReader(textWidget.getText());
		
		try {
			// Creates a file writer to write back to the original file.
			writer = new FileWriter(uri);
			LogUtil.log.info("Saving the contents of editor {0} to file: {1}", part.getLabel(), uri); //$NON-NLS-1$
			
			// Reads the editor's contents one KB at a time and write it to the file.
			char[] buffer = new char[1024];
			for (;;) {
				int charsRead = reader.read(buffer);
				if (charsRead == -1)
					break;
				writer.write(buffer, 0, charsRead);
			}
			
			// If writing was successful (no exceptions), sets the editor as clean.
			setDirty(false);
		}
		
		// In case of I/O errors, shows an error dialog.
		catch (IOException e) {
			String classKey = getClassKey();
			LogUtil.log.error("Unable to read file from URI: {0}.", e, uri); //$NON-NLS-1$
			final String statusMsg = Messages.getString("gui." + classKey + ".error.write.status"); //$NON-NLS-1$ //$NON-NLS-2$
			final String errorTitle = Messages.getString("gui." + classKey + ".error.write.title"); //$NON-NLS-1$ //$NON-NLS-2$
			final String errorMessage = Messages.getFormattedString("gui." + classKey + ".error.write.message", uri); //$NON-NLS-1$ //$NON-NLS-2$
			DialogUtil.displayError(activator.getBundleId(), statusMsg, errorTitle, errorMessage);
		}
		finally {
			// In any case, tries to close the reader if it was open.
			try {
				if (reader != null)
					reader.close();
				if (writer != null)
					writer.close();
			}
			catch (IOException e) {
				LogUtil.log.error("Unable to close readers/writers while saving file: {0}.", e, uri); //$NON-NLS-1$
			}
		}
	}

	/** @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent) */
	@Override
	public void modifyText(ModifyEvent e) {
		String label = part.getLabel();
		LogUtil.log.debug("Changes detected in the editor for {0}. Marking it as dirty...", label); //$NON-NLS-1$

		// When there's a change, marks the editor as dirty.
		setDirty(true);
	}

	/**
	 * Retrieves the contents of the file referred to by the given URI and returns it.
	 * 
	 * @param uri
	 *          URI referring to the file whose contents should be retrieved.
	 * @return The contents of the file referred to by the given URI.
	 */
	private String retrieveFileContents(String uri) {
		FileReader reader = null;
		StringBuffer stringBuffer = new StringBuffer();
		
		try {
			// Opens a file reader for the given URI.
			reader = new FileReader(uri);
			
			// Reads the file's contents one KB at a time and add it to a string buffer.
			char[] buffer = new char[1024];
			for (;;) {
				int charsRead = reader.read(buffer);
				if (charsRead == -1)
					break;
				stringBuffer.append(buffer, 0, charsRead);
			}
		}
		
		// In case of I/O errors, shows an error dialog.
		catch (IOException e) {
			String classKey = getClassKey();
			LogUtil.log.error("Unable to read file from URI: {0}.", e, uri); //$NON-NLS-1$
			final String statusMsg = Messages.getString("gui." + classKey + ".error.read.status"); //$NON-NLS-1$ //$NON-NLS-2$
			final String errorTitle = Messages.getString("gui." + classKey + ".error.read.title"); //$NON-NLS-1$ //$NON-NLS-2$
			final String errorMessage = Messages.getFormattedString("gui." + classKey + ".error.read.message", uri); //$NON-NLS-1$ //$NON-NLS-2$
			DialogUtil.displayError(activator.getBundleId(), statusMsg, errorTitle, errorMessage);
			return null;
		}
		finally {
			// In any case, tries to close the reader if it was open.
			try {
				if (reader != null)
					reader.close();
			}
			catch (IOException e) {
				LogUtil.log.error("Unable to close reader while reading file: {0}.", e, uri); //$NON-NLS-1$
			}
		}
		
		// Returns the contents of the string buffer.
		return stringBuffer.toString();
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
}
