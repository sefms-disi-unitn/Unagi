package it.unitn.disi.unagi.rcpapp.wizards;

import it.unitn.disi.unagi.rcpapp.nls.Messages;
import it.unitn.disi.util.logging.LogUtil;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
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
@Creatable
public class CreateNewProjectBasicWizardPage extends WizardPage {
	/** Container associated to this wizard page. */
	private Composite container;

	/** Text field for the name of the project. */
	private Text nameField;

	/** Constructor. */
	public CreateNewProjectBasicWizardPage() {
		super(Messages.getString("gui.createNewProjectWizard.basicPage.title")); //$NON-NLS-1$
		setTitle(Messages.getString("gui.createNewProjectWizard.basicPage.title")); //$NON-NLS-1$
	}

	/** @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite) */
	@Override
	public void createControl(Composite parent) {
		LogUtil.log.debug("Initializing \"Basic\" page for the \"Create New Project\" wizard."); //$NON-NLS-1$

		// Creates a new container using a grid layout with 3 columns.
		container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;

		// Creates the label for the name field.
		Label nameLabel = new Label(container, SWT.NULL);
		nameLabel.setText(Messages.getString("gui.createNewProjectWizard.basicPage.field.name")); //$NON-NLS-1$

		// Creates the name field.
		nameField = new Text(container, SWT.BORDER | SWT.SINGLE);
		nameField.setText(""); //$NON-NLS-1$

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
	 * Processes the event of a key being pressed when the name field is active so we can check if all required fields
	 * have been filled and complete the wizard page.
	 * 
	 * @param e
	 *          The key event passed on by the event dispatcher.
	 */
	private final void processNameFieldKeyReleased(KeyEvent e) {
		// TODO: issue #17
		// https://github.com/sefms-disi-unitn/Unagi/issues/17

		// Checks if all required fields are filled to set the page complete flag.
		setPageComplete(isAllFilled());
	}

	/**
	 * Checks if all required fields were filled. This information is needed to indicate if the wizard page is complete
	 * and the user can move on to the next page. Required fields are name and folder.
	 * 
	 * @return <code>true</code> if all required fields were filled, <code>false</code> otherwise.
	 */
	private boolean isAllFilled() {
		return (!nameField.getText().isEmpty());
	}
}
