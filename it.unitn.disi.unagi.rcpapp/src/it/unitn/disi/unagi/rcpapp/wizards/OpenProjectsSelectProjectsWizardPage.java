package it.unitn.disi.unagi.rcpapp.wizards;

import it.unitn.disi.unagi.rcpapp.nls.Messages;
import it.unitn.disi.util.logging.LogUtil;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

/**
 * A page of the "Open Projects" wizard that shows the available closed projects for the user, allowing her to open any
 * of them.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
@Creatable
public class OpenProjectsSelectProjectsWizardPage extends WizardPage implements SelectionListener {
	/** The application's workspace, injected automatically by Eclipse. */
	@Inject
	private IWorkspace workspace;

	/** Container associated to this wizard page. */
	private Composite container;

	/** A map that associates project names with projects. */
	private Map<String, IProject> projects = new TreeMap<>();

	/** The GUI component that shows the closed projects in a list, allowing for multiple selection. */
	private List projectsList;

	/** Constructor. */
	public OpenProjectsSelectProjectsWizardPage() {
		super(Messages.getString("gui.openProjectsWizard.selectProjectsPage.title")); //$NON-NLS-1$
		setTitle(Messages.getString("gui.openProjectsWizard.selectProjectsPage.title")); //$NON-NLS-1$
	}

	/**
	 * Builds the map of projects from the list of closed projects in the workspace. This method is called automatically
	 * by Eclipse.
	 */
	@PostConstruct
	public void buildProjectIndex() {
		for (IProject project : workspace.getRoot().getProjects())
			if (!project.isOpen())
				projects.put(project.getName(), project);
	}

	/** @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite) */
	@Override
	public void createControl(Composite parent) {
		LogUtil.log.debug("Initializing \"Select Project(s)\" page for the \"Open Project(s)\" wizard."); //$NON-NLS-1$

		// Creates a new container using a grid layout.
		container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);

		// Creates the projects list, adding all closed projects to it.
		projectsList = new List(container, SWT.MULTI);
		projectsList.setItems(projects.keySet().toArray(new String[0]));

		// Configures the projects list to fill the entire space of the wizard.
		GridData gd = new GridData(GridData.FILL_BOTH);
		projectsList.setLayoutData(gd);

		// Sets the wizard page itself as a listener to changes in the projects list.
		projectsList.addSelectionListener(this);

		// Sets the created container as the control for this page so Eclipse is able to integrate this page to the wizard.
		setControl(container);

		// Indicates that the user is not yet finished with this page.
		setPageComplete(false);
	}

	/**
	 * Returns a list with the projects that were selected by the user in the wizard page.
	 * 
	 * @return A list of selected projects.
	 */
	protected java.util.List<IProject> getSelectedProjects() {
		java.util.List<IProject> selectedProjects = new ArrayList<>();
		int[] selectedIdxs = projectsList.getSelectionIndices();
		for (int idx : selectedIdxs) {
			String name = projectsList.getItem(idx);
			selectedProjects.add(projects.get(name));
		}
		return selectedProjects;
	}

	/**
	 * Checks if all required fields were filled. This information is needed to indicate if the wizard page is complete
	 * and the user can move on to the next page.
	 * 
	 * @return <code>true</code> if all required fields were filled, <code>false</code> otherwise.
	 */
	private boolean isAllFilled() {
		return (projectsList.getItemCount() > 0);
	}

	/** @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent) */
	@Override
	public void widgetSelected(SelectionEvent e) {
		// Checks if all required fields are filled to set the page complete flag.
		setPageComplete(isAllFilled());
	}

	/** @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent) */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		if (isAllFilled())
			this.getWizard().performFinish();
	}
}
