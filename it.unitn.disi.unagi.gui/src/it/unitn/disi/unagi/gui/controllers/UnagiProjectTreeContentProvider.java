package it.unitn.disi.unagi.gui.controllers;

import it.unitn.disi.unagi.domain.core.UnagiProject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	/** Mapping between open projects and their children. */
	private Map<UnagiProject, UnagiProjectChild[]> openProjectChildren = new HashMap<UnagiProject, UnagiProjectChild[]>();

	/** The list of open projects. */
	private List<UnagiProject> openProjects;

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
		Collection<? extends UnagiProject> newOpenProjects = (Collection<? extends UnagiProject>) newInput;
		if (newOpenProjects == null)
			newOpenProjects = new ArrayList<UnagiProject>();

		// Stores a list of projects as the model for the project tree.
		openProjects = new ArrayList<UnagiProject>(newOpenProjects);

		// Remove from the project-children mapping any project that has been closed.
		List<UnagiProject> closedProjects = new ArrayList<UnagiProject>();
		for (UnagiProject project : openProjectChildren.keySet())
			if (!openProjects.contains(project))
				closedProjects.add(project);
		for (UnagiProject project : closedProjects)
			openProjectChildren.remove(project);
	}

	/** @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object) */
	@Override
	public Object[] getElements(Object inputElement) {
		return openProjects.toArray();
	}

	/** @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object) */
	@Override
	public Object[] getChildren(Object parentElement) {
		// Checks if the parent element is a UnagiProject.
		if (parentElement instanceof UnagiProject) {
			UnagiProject project = (UnagiProject) parentElement;

			// Checks if the children for this project have already been created.
			UnagiProjectChild[] children = openProjectChildren.get(parentElement);
			if (children == null) {
				// If they haven't been created yet, do it. Create one child for each category.
				UnagiProjectChildCategory[] categories = UnagiProjectChildCategory.values();
				children = new UnagiProjectChild[categories.length];
				int i = 0;
				for (UnagiProjectChildCategory category : categories)
					children[i++] = new UnagiProjectChild(project, category);

				// Put the children at the children's map and return it.
				openProjectChildren.put(project, children);
			}

			// Return the children (the one from the map or the newly created ones).
			return children;
		}

		// If the class of the element hasn't been recognized above, this element has no children.
		return null;
	}

	/** @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object) */
	@Override
	public Object getParent(Object element) {
		// Checks if the element is a direct Unagi Project child and return the project.
		if (element instanceof UnagiProjectChild) { return ((UnagiProjectChild) element).getParent(); }

		// If the class of the element hasn't been recognized above, this element has no parent.
		return null;
	}

	/** @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object) */
	@Override
	public boolean hasChildren(Object element) {
		// Unagi project's have children.
		return (element instanceof UnagiProject);
	}
}
