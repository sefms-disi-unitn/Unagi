package it.unitn.disi.unagi.gui.controllers;

import it.unitn.disi.unagi.domain.core.UnagiProject;
import it.unitn.disi.unagi.gui.models.ProjectTreeElement;
import it.unitn.disi.unagi.gui.models.UnagiProjectPTElement;

import java.util.Collection;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The controller for the project tree GUI. Receives the list of open projects from the application and provides a tree
 * structure that is shown by the Project View SWT tree.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class UnagiProjectTreeContentProvider implements ITreeContentProvider {
	/** Map of projects and their tree model representations. */
	private SortedMap<UnagiProject, UnagiProjectPTElement> treeModel = new TreeMap<UnagiProject, UnagiProjectPTElement>();

	/** @see org.eclipse.jface.viewers.IContentProvider#dispose() */
	@Override
	public void dispose() {}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
	 *      java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// The input is supposed to be a collection of UnagiProject instances. If null, use an empty list.
		SortedSet<UnagiProject> newOpenProjects = (newInput == null) ? new TreeSet<UnagiProject>() : new TreeSet<UnagiProject>((Collection<? extends UnagiProject>) newInput);

		// Determines which projects are new and which should be closed.
		Set<UnagiProject> openProjects = treeModel.keySet();
		Set<UnagiProject> projectsToClose = new TreeSet<UnagiProject>(openProjects);
		for (UnagiProject project : newOpenProjects) {
			// If the project is already open, remove from the set of projects to close.
			if (openProjects.contains(project))
				projectsToClose.remove(project);
			
			// If the project is not already open, then it's new. Open it, also adding it to the tree model.
			else {
				treeModel.put(project, new UnagiProjectPTElement(project));
			}
		}
		
		// Closes projects that were marked to be closed.
		for (UnagiProject project : projectsToClose)
			treeModel.remove(project);
	}

	/** @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object) */
	@Override
	public Object[] getElements(Object inputElement) {
		Collection<UnagiProjectPTElement> elems = treeModel.values();

		// According to the superclass, the returned array should not contain the element supplied as a parameter. 
		if (elems.contains(inputElement)) elems.remove(inputElement);
		
		// Returns the project tree elements in an array.
		return elems.toArray();
	}

	/** @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object) */
	@Override
	public Object[] getChildren(Object parentElement) {
		// If the element belongs to the Project Tree model, ask it for its children.
		if (parentElement instanceof ProjectTreeElement)
			return ((ProjectTreeElement) parentElement).getChildren();
		
		// Otherwise it's an unknown element. By default it has no children.
		return null;
	}

	/** @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object) */
	@Override
	public Object getParent(Object element) {
		// If the element belongs to the Project Tree model, ask it for its children.
		if (element instanceof ProjectTreeElement)
			return ((ProjectTreeElement) element).getParent();
		
		// Otherwise it's an unknown element. By default it has no parent.
		return null;
	}

	/** @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object) */
	@Override
	public boolean hasChildren(Object element) {
		// If the element belongs to the Project Tree model, ask it for its children.
		if (element instanceof ProjectTreeElement)
			return ((ProjectTreeElement) element).hasChildren();
		
		// Otherwise it's an unknown element. By default it has no children.
		return false;
	}
}
