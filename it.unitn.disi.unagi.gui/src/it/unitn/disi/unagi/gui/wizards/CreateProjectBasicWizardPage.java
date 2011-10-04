package it.unitn.disi.unagi.gui.wizards;

import it.unitn.disi.unagi.application.services.Unagi;

import java.io.File;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * A page of the "Create Project" wizard that asks the user for the basic information on the project, such as name and
 * containing folder.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CreateProjectBasicWizardPage extends WizardPage {
	/** The Unagi application. */
	private Unagi unagi = Unagi.getInstance();

	/** Container associated to this wizard page. */
	private Composite container;

	/** Text field for the name of the project. */
	private Text nameField;

	/** File field for the path of the file onto which the project will be saved. */
	private DirectoryFieldEditor folderField;

	/** Constructor. */
	protected CreateProjectBasicWizardPage() {
		super("New Unagi Project");
		setTitle("Basic project information");
	}

	/** @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite) */
	@Override
	public void createControl(Composite parent) {
		// Creates a new container using a grid layout with 3 columns.
		container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;

		// Creates the label for the name field.
		Label nameLabel = new Label(container, SWT.NULL);
		nameLabel.setText("Project name:");

		// Creates the name field.
		nameField = new Text(container, SWT.BORDER | SWT.SINGLE);
		nameField.setText("");

		// Configures the name field to use two columns of the grid.
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		nameField.setLayoutData(gd);

		// Listen to key release events to check if the user is finished with the page.
		nameField.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				processNameFieldKeyReleased(e);
			}
		});

		// Creates the file field, which is composed of three parts and so automatically fills three columns.
		File lastFolderUsed = new File(unagi.getProperty(Unagi.CFG_LAST_FOLDER_FILE_DIALOGS));
		folderField = new DirectoryFieldEditor("file", "Save to folder:", container);
		folderField.setFilterPath(lastFolderUsed);

		// Listen to modified values of this field to check if the user is finished with the page.
		folderField.getTextControl(container).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				processFolderFieldModifyText(e);
			}
		});

		// Sets the created container as the control for this page so Eclipse is able to integrate this page to the wizard.
		setControl(container);

		// Indicates that the user is not yet finished with this page.
		setPageComplete(false);
	}

	/**
	 * Returns the name of the project, which was filled in by the user in the name field.
	 * 
	 * @return A string containing whatever the user filled in the name field.
	 */
	protected String getProjectName() {
		return nameField.getText();
	}

	/**
	 * Returns the path to the folder that will contain the project, which was filled in by the user in the folder field.
	 * 
	 * @return A string containing whatever the user filled in the folder field.
	 */
	protected String getFolderPath() {
		return folderField.getStringValue();
	}

	/**
	 * Processes the event of a key being pressed when the name field is active so we can check if all required fields
	 * have been filled and complete the wizard page.
	 * 
	 * @param e
	 *          The key event passed on by the event dispatcher.
	 */
	private final void processNameFieldKeyReleased(KeyEvent e) {
		// Checks if all required fields are filled to set the page complete flag.
		setPageComplete(isAllFilled());
	}

	/**
	 * Processes the event of the value of the folder field changing so we can check if all required fields have been
	 * filled and complete the wizard page. Also memorizes the last used folder in this type of dialog.
	 * 
	 * @param e
	 */
	private final void processFolderFieldModifyText(ModifyEvent e) {
		// Checks if all required fields are filled to set the page complete flag.
		setPageComplete(isAllFilled());

		// Changes the last folder used configuration.
		String value = folderField.getStringValue();
		if (!value.isEmpty()) {
			File lastFolderUsed = new File(value);

			// If the folder indicated by the file field doesn't exist or is not a folder, try the parent.
			if ((!lastFolderUsed.exists()) || (!lastFolderUsed.isDirectory())) {
				File parent = lastFolderUsed.getParentFile();

				// Also checks if the parent exists and is a folder. If it's not, do not change the last folder used.
				if ((parent != null) && parent.exists() && parent.isDirectory())
					lastFolderUsed = parent;
				else lastFolderUsed = null;
			}

			// If we found any appropriate "last folder used", change the configuration and the file field.
			if (lastFolderUsed != null) {
				unagi.setProperty(Unagi.CFG_LAST_FOLDER_FILE_DIALOGS, lastFolderUsed.getAbsolutePath());
				folderField.setFilterPath(lastFolderUsed);
			}
		}
	}

	/**
	 * Checks if all required fields were filled. This information is needed to indicate if the wizard page is complete
	 * and the user can move on to the next page. Required fields are name and folder.
	 * 
	 * @return <code>true</code> if all required fields were filled, <code>false</code> otherwise.
	 */
	private boolean isAllFilled() {
		return (!(nameField.getText().isEmpty() || folderField.getStringValue().isEmpty()));
	}
}
