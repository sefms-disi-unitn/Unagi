package it.unitn.disi.unagi.rcpapp.views;

import it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement;
import it.unitn.disi.unagi.rcpapp.views.providers.ProjectTreeContentProvider;
import it.unitn.disi.unagi.rcpapp.views.providers.ProjectTreeLabelProvider;
import it.unitn.disi.util.logging.LogUtil;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * An Eclipse view that allows users to explore Unagi projects and their contents.
 * 
 * This GUI component implements several listeners in order to respond to changes in resources, changes in selection,
 * popup menu activations and clicks in the project tree.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class UnagiProjectExplorerView implements IResourceChangeListener, ISelectionChangedListener, MenuDetectListener, MouseListener {
	/** ID of this view. */
	public static final String VIEW_ID = "it.unitn.disi.unagi.rcpapp.part.projectExplorer"; //$NON-NLS-1$

	/** The application's workspace. */
	@Inject
	private IWorkspace workspace;

	/** The workbench's selection service. */
	@Inject
	private ESelectionService selectionService;

	/** The workbench's menu service. */
	@Inject
	private EMenuService menuService;

	/** The project tree GUI component. */
	private TreeViewer projectsTree;

	/**
	 * Initialization method, called automatically by Eclipse after an instance of this class is constructed. All
	 * parameters of this method are automatically injected by Eclipse's DI framework.
	 * 
	 * @param parent
	 *          The GUI element that is hierarchically above this view.
	 * @param contentProvider
	 *          The content provider for the project tree.
	 * @param labelProvider
	 *          The label provider for the project tree.
	 */
	@PostConstruct
	public void init(Composite parent, ProjectTreeContentProvider contentProvider, ProjectTreeLabelProvider labelProvider) {
		LogUtil.log.debug("Initializing Unagi's Project Explorer view."); //$NON-NLS-1$

		// Retrieves the list of projects from the workspace.
		IProject[] projects = workspace.getRoot().getProjects();

		// Creates a tree viewer to show the list of open projects.
		projectsTree = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		projectsTree.setContentProvider(contentProvider);
		projectsTree.setLabelProvider(labelProvider);

		// Expands the tree and sets the list of open projects as the input for the tree.
		projectsTree.setAutoExpandLevel(2);
		projectsTree.setInput(projects);

		// Registers itself as a listener to changes in the projects tree.
		projectsTree.addSelectionChangedListener(this);

		// Registers itself as a menu detection listener to create appropriate pop-up menus.
		projectsTree.getControl().addMenuDetectListener(this);

		// Registers itself as a mouse listener to respond to double clicks.
		projectsTree.getControl().addMouseListener(this);

		// Registers itself as a listener to changes in the workspace.
		LogUtil.log.debug("Registering the Project Explorer as a resource change listener in the workspace."); //$NON-NLS-1$
		workspace.addResourceChangeListener(this);
	}

	/**
	 * Sets the focus in the main GUI control of this view, the project tree. This method is called automatically by
	 * Eclipse.
	 */
	@Focus
	private void setFocus() {
		projectsTree.getControl().setFocus();
	}

	/**
	 * Disposes the view and its components. This method is called automatically by Eclipse.
	 */
	@PreDestroy
	public void dispose() {
		projectsTree.getControl().dispose();
	}

	/** @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent) */
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		// FIXME: learn how to work with deltas for a more efficient refresh.
		// Currently, anything that is done to the workspace resets the entire tree. Fix that.
		if (event.getSource() instanceof IWorkspace)
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					projectsTree.setInput(workspace.getRoot().getProjects());
				}
			});
	}

	/** @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent) */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			selectionService.setSelection(structuredSelection.size() == 1 ? structuredSelection.getFirstElement() : structuredSelection.toArray());
		}
	}

	/** @see org.eclipse.swt.events.MenuDetectListener#menuDetected(org.eclipse.swt.events.MenuDetectEvent) */
	@Override
	public void menuDetected(MenuDetectEvent e) {
		LogUtil.log.debug("Project tree context menu about to show. Dynamically choosing the application pop-up menu."); //$NON-NLS-1$
		Control control = projectsTree.getControl();

		// Obtains the selected elements from the project tree. Continues if at least one element was selected.
		IStructuredSelection selection = (IStructuredSelection) projectsTree.getSelection();
		if (!selection.isEmpty()) {
			// If the first element belongs to the Project Tree model, ask it for the ID of the corresponding pop-up menu.
			Object firstElement = selection.getFirstElement();
			if (firstElement instanceof AbstractProjectTreeElement) {
				AbstractProjectTreeElement ptElement = (AbstractProjectTreeElement) firstElement;
				String popupMenuId = ptElement.getPopupMenuId();

				// Loads the pop-up menu and associates it to the tree.
				LogUtil.log.debug("Selected element ({0}) returned pop-up ID: {1}", ptElement.getClass().getSimpleName(), popupMenuId); //$NON-NLS-1$
				menuService.registerContextMenu(control, popupMenuId);
			}
		}
	}

	/** @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent) */
	@Override
	public void mouseDoubleClick(MouseEvent event) {
		// Obtains the selected elements from the project tree. Continues if at least one element was selected.
		IStructuredSelection selection = (IStructuredSelection) projectsTree.getSelection();
		if (!selection.isEmpty()) {

			// FIXME: implement response to double-click.
			System.out.println("###### Double clicked a: " + selection.getFirstElement()); //$NON-NLS-1$

		}
	}

	/** @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent) */
	@Override
	public void mouseDown(MouseEvent event) {}

	/** @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent) */
	@Override
	public void mouseUp(MouseEvent event) {}
}
