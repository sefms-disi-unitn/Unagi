package it.unitn.disi.unagi.gui.wizards;

import it.unitn.disi.unagi.domain.core.UnagiProject;
import it.unitn.disi.unagi.gui.nls.Messages;

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
 * A page of the "Create Requirements Model" wizard that asks the user for the basic information on the requirements,
 * such as its name.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CreateRequirementsModelBasicWizardPage extends WizardPage {
	/** Container associated to this wizard page. */
	private Composite container;
	
	/** Project in which the requirements model will be created. */
	private UnagiProject project;

	/** Text field for the name of the project. */
	private Text projectField;

	/** Text field for the name of the model. */
	private Text nameField;
	
	/** Text field for the base package for this project. */
	private Text basePackageField;

	/** Constructor. */
	protected CreateRequirementsModelBasicWizardPage(UnagiProject project) {
		super(Messages.getString("gui.wizard.createRequirementsModel.basicPage.title")); //$NON-NLS-1$
		setTitle(Messages.getString("gui.wizard.createRequirementsModel.basicPage.title")); //$NON-NLS-1$
		this.project = project;
	}

	/** @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite) */
	@Override
	public void createControl(Composite parent) {
		// Creates a new container using a grid layout with 2 columns.
		container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;

		// Creates the label for the project field.
		Label projectLabel = new Label(container, SWT.NULL);
		projectLabel.setText(Messages.getString("gui.wizard.createRequirementsModel.basicPage.label.project")); //$NON-NLS-1$

		// Creates the project field, which is not editable (can't change project here).
		projectField = new Text(container, SWT.BORDER | SWT.SINGLE);
		projectField.setText(project.getName());
		projectField.setEditable(false);
		projectField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Creates the label for the name field.
		Label nameLabel = new Label(container, SWT.NULL);
		nameLabel.setText(Messages.getString("gui.wizard.createRequirementsModel.basicPage.label.name")); //$NON-NLS-1$

		// Creates the name field and focuses on it.
		nameField = new Text(container, SWT.BORDER | SWT.SINGLE);
		nameField.setText(""); //$NON-NLS-1$
		nameField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameField.forceFocus();

		// Listen to key release events to check if the user is finished with the page.
		nameField.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				// Checks that the name field was filled and, therefore, the page is complete.
				setPageComplete(! nameField.getText().isEmpty());
			}
		});
		
		// Creates the label for the base package field.
		Label basePackageLabel = new Label(container, SWT.NULL);
		basePackageLabel.setText(Messages.getString("gui.wizard.createRequirementsModel.basicPage.label.basePackage")); //$NON-NLS-1$
		
		// Creates the base package field.
		basePackageField = new Text(container, SWT.BORDER | SWT.SINGLE);
		basePackageField.setText(""); //$NON-NLS-1$
		basePackageField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Sets the created container as the control for this page so Eclipse is able to integrate this page to the wizard.
		setControl(container);

		// Indicates that the user is not yet finished with this page.
		setPageComplete(false);
	}

	/**
	 * Returns the name of the model, which was filled in by the user in the name field.
	 * 
	 * @return A string containing whatever the user filled in the name field.
	 */
	protected String getModelName() {
		return nameField.getText();
	}
	
	/**
	 * Returns the base package of the model, which was filled in by the user in the base package field.
	 * 
	 * @return A string containing whatever the user filled in the base package field.
	 */
	protected String getBasePackage() {
		return basePackageField.getText();
	}
}
