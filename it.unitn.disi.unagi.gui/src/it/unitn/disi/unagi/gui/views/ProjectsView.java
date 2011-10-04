package it.unitn.disi.unagi.gui.views;

import it.unitn.disi.unagi.application.services.ManageProjectsService;
import it.unitn.disi.unagi.application.services.Unagi;
import it.unitn.disi.unagi.domain.core.UnagiProject;
import it.unitn.disi.unagi.gui.actions.CloseProjectsAction;
import it.unitn.disi.unagi.gui.actions.NewRequirementsModelAction;
import it.unitn.disi.unagi.gui.controllers.UnagiProjectChild;
import it.unitn.disi.unagi.gui.controllers.UnagiProjectLabelProvider;
import it.unitn.disi.unagi.gui.controllers.UnagiProjectTreeContentProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * A view that shows the list of open projects and allows the user to manipulate them and their elements.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class ProjectsView extends ViewPart implements IPropertyChangeListener {
	/** The Unagi application. */
	private Unagi unagi = Unagi.getInstance();

	/** The "Manage Projects" service. */
	private ManageProjectsService manageProjectsService = unagi.getManageProjectsService();

	/** The open projects tree. */
	private TreeViewer openProjectsTree;

	/**
	 * Method called by the workbench to tell the view to create its visual components.
	 * 
	 * @param parent
	 *          Parent component for the view.
	 */
	@Override
	public void createPartControl(Composite parent) {
		// Obtains the current open projects.
		List<UnagiProject> openProjects = new ArrayList<UnagiProject>();
		openProjects.addAll(manageProjectsService.getOpenProjects());

		// Creates a tree viewer to show the list of open projects.
		openProjectsTree = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		openProjectsTree.setContentProvider(new UnagiProjectTreeContentProvider());
		openProjectsTree.setLabelProvider(new UnagiProjectLabelProvider());

		// Expands the tree and sets the list of open projects as the input for the tree.
		openProjectsTree.setAutoExpandLevel(2);
		openProjectsTree.setInput(openProjects);

		// Configures a dynamic context menu for the projects tree.
		createDynamicContextMenu();

		// Registers the view as a listener for property changes regarding the open projects list.
		manageProjectsService.addPropertyChangeListener(this);
	}

	/**
	 * Method called by the workbench to tell the view to take focus within the workbench.
	 */
	@Override
	public void setFocus() {
		// Forwards the focus request to the tree viewer.
		openProjectsTree.getControl().setFocus();
	}

	/** @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent) */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		openProjectsTree.getContentProvider().inputChanged(openProjectsTree, event.getOldValue(), event.getNewValue());
		openProjectsTree.refresh();
	}

	/**
	 * Dynamically creates context menus (pop-up menus that are shown when you right-click an element) for elements of the
	 * project tree, such as the Unagi Project itself or any of its elements.
	 */
	private void createDynamicContextMenu() {
		// Creates a menu manager and configures it to clear itself when shown.
		final MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);

		// Then adds a listener to add menu actions when the menu is about to be shown.
		menuManager.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				// Obtains the selected elements from the project tree. Continues if at least one element was selected.
				IStructuredSelection selection = (IStructuredSelection) openProjectsTree.getSelection();
				if (!selection.isEmpty()) {
					// Obtains info about tree selection: the first element, its class and if more than 1 element was selected.
					Object firstElement = selection.getFirstElement();
					Class<? extends Object> clazz = firstElement.getClass();
					boolean singleSelection = (selection.size() == 1);

					/*
					 * CONTEXT MENUS: the following (long) code checks for the type of the first selected element and dynamically
					 * creates the context menu based on the type of this element. Some actions can be applied to all elements of
					 * the same type, whereas other actions will only be included if only one element is selected.
					 */

					/* SELECTED: a Unagi Project. */
					if (UnagiProject.class.equals(clazz)) {
						// Builds a list of all selected projects for the actions that can be applied to multiple elements.
						List<UnagiProject> selectedProjects = filterSelectionByClass(selection, UnagiProject.class);

						// Adds the "Close projects" action to the menu.
						menuManager.add(new CloseProjectsAction(selectedProjects));
					}

					/* SELECTED: a Unagi Project child. */
					else if (UnagiProjectChild.class.equals(clazz)) {
						UnagiProjectChild child = (UnagiProjectChild) firstElement;

						/* SELECTED: the "Models" child of Unagi Project. */
						switch (child.getCategory()) {
						case MODELS:
							// Single-element action: "New requirements model".
							if (singleSelection) menuManager.add(new NewRequirementsModelAction(child.getParent()));
						}
					}
				}
			}
		});

		// Finally, associates the menu manager with the open projects tree.
		openProjectsTree.getControl().setMenu(menuManager.createContextMenu(openProjectsTree.getControl()));
	}

	/**
	 * TODO: document this method.
	 * 
	 * @param selection
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> List<T> filterSelectionByClass(IStructuredSelection selection, Class<T> clazz) {
		List<T> filteredList = new ArrayList<T>();
		for (Iterator<?> iter = selection.iterator(); iter.hasNext();) {
			Object elem = iter.next();
			if ((elem != null) && (elem.getClass().equals(clazz)))
				filteredList.add((T) elem);
		}
		return filteredList;
	}
}
