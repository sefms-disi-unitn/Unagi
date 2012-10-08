package it.unitn.disi.unagi.rcpapp.views;

import it.unitn.disi.util.logging.LogUtil;

import java.io.FileReader;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.MInput;
import org.eclipse.swt.SWT;
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
public class EcoreEditorPart {
	/** ID of this part. */
	public static final String PART_ID = "it.unitn.disi.unagi.rcpapp.views.EcoreEditorPart"; //$NON-NLS-1$

	/** TODO: document this field. */
	private Composite parent;

	/** TODO: document this field. */
	private MInput input;

	/** TODO: document this field. */
	private Text textWidget;

	/**
	 * TODO: document this method.
	 * 
	 * @param parent
	 */
	@PostConstruct
	public void init(Composite parent, MInput input) {
		LogUtil.log.debug("Initializing an ECore editor."); //$NON-NLS-1$

		this.parent = parent;
		this.input = input;

		// Uses a single text widget for the entire editor.
		parent.setLayout(new FillLayout());
		textWidget = new Text(parent, SWT.MULTI | SWT.WRAP);

		// Retrieves the contents of the file that is being edited.
		textWidget.setText(retrieveFileContents(input.getInputURI()));
	}

	/**
	 * TODO: document this method.
	 */
	@Focus
	private void setFocus() {
		textWidget.setFocus();
	}

	/**
	 * TODO: document this method.
	 * 
	 * @param uri
	 * @return
	 */
	private String retrieveFileContents(String uri) {
		FileReader reader = null;
		StringBuffer stringBuffer = null;
		try {
			reader = new FileReader(uri);
			stringBuffer = new StringBuffer();
			char[] buffer = new char[1024];
			for (;;) {
				int charsRead = reader.read(buffer);
				if (charsRead == -1)
					break;
				stringBuffer.append(buffer, 0, charsRead);
			}
		}
		catch (IOException e) {
			System.err.println("Unable to read file " + uri); //$NON-NLS-1$
			e.printStackTrace();
			return null;
		}
		finally {
			try {
				if (reader != null)
					reader.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return stringBuffer.toString();
	}
}
