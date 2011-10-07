package it.unitn.disi.unagi.gui.views;

import it.unitn.disi.unagi.application.services.ManageProjectsService;
import it.unitn.disi.unagi.application.services.Unagi;
import it.unitn.disi.unagi.domain.core.UnagiProject;
import it.unitn.disi.unagi.gui.controllers.UnagiProjectLabelProvider;
import it.unitn.disi.unagi.gui.controllers.UnagiProjectTreeContentProvider;
import it.unitn.disi.unagi.gui.models.ModelProjectTreeElement;
import it.unitn.disi.unagi.gui.models.ProjectTreeElement;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;
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
		
		final IWorkbenchPage page = getSite().getPage();
		openProjectsTree.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				// Captures the selected element.
				IStructuredSelection selection = (IStructuredSelection) openProjectsTree.getSelection();
				if (! selection.isEmpty()) {
					Object firstElement = selection.getFirstElement();
					
					// If it's a model, open it in the text editor.
					if (firstElement instanceof ModelProjectTreeElement) {
						ModelProjectTreeElement modelElem = (ModelProjectTreeElement) firstElement;
						IFileStore fileStore = EFS.getLocalFileSystem().getStore(new Path(modelElem.getModel().getFile().getAbsolutePath()));
						IEditorInput input = new FileStoreEditorInput(fileStore);
						try {
							
							page.openEditor(input, "org.eclipse.emf.ecore.presentation.EcoreEditorID");
						}
						catch (PartInitException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			}
		});
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
					// If the first element belongs to the Project Tree model, ask it for a list of applicable actions.
					Object firstElement = selection.getFirstElement();
					if (firstElement instanceof ProjectTreeElement)
						for (IAction action : ((ProjectTreeElement) firstElement).getApplicableActions(selection))
							menuManager.add(action);
				}
			}
		});

		// Finally, associates the menu manager with the open projects tree.
		openProjectsTree.getControl().setMenu(menuManager.createContextMenu(openProjectsTree.getControl()));
	}
}
