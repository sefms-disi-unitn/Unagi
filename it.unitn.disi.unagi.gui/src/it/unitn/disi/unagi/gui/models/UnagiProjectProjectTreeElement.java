package it.unitn.disi.unagi.gui.models;

import it.unitn.disi.unagi.domain.core.UnagiProject;
import it.unitn.disi.unagi.gui.actions.CloseProjectsAction;
import it.unitn.disi.unagi.gui.utils.CollectionsUtil;
import it.unitn.disi.unagi.gui.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;

/**
 * TODO: document this type.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class UnagiProjectProjectTreeElement extends ProjectTreeElement {
	/** TODO: document this field. */
	private UnagiProject project;

	private UnagiProjectChildProjectTreeElement[] children;

	/** Constructor. */
	public UnagiProjectProjectTreeElement(UnagiProject project) {
		this.project = project;

		// Create a child of each category for the project to better organize the project's contents.
		UnagiProjectChildCategory[] categories = UnagiProjectChildCategory.values();
		children = new UnagiProjectChildProjectTreeElement[categories.length];
		int i = 0;
		for (UnagiProjectChildCategory category : categories)
			children[i++] = new UnagiProjectChildProjectTreeElement(project, category);
	}

	/** Getter for project. */
	public UnagiProject getProject() {
		return project;
	}

	/** @see it.unitn.disi.unagi.gui.models.ProjectTreeElement#getLabel() */
	@Override
	public String getLabel() {
		// Use the project name as label.
		return project.getName();
	}

	/** @see it.unitn.disi.unagi.gui.models.ProjectTreeElement#getIcon() */
	@Override
	public Image getIcon() {
		// All projects use the same icon.
		return ImageUtil.loadImage("/icons/entity-unagiproject.png");
	}

	/** @see it.unitn.disi.unagi.gui.models.ProjectTreeElement#hasChildren() */
	@Override
	public boolean hasChildren() {
		// Projects have children.
		return true;
	}

	/** @see it.unitn.disi.unagi.gui.models.ProjectTreeElement#getChildren() */
	@Override
	public Object[] getChildren() {
		return children;
	}

	/** @see it.unitn.disi.unagi.gui.models.ProjectTreeElement#getApplicableActions(org.eclipse.jface.viewers.IStructuredSelection) */
	@Override
	public List<IAction> getApplicableActions(IStructuredSelection selection) {
		List<IAction> actionList = new ArrayList<IAction>();
		
		// Builds a list of all selected projects for the actions that can be applied to multiple elements.
		List<UnagiProjectProjectTreeElement> selectedElements = CollectionsUtil.filterSelectionByClass(selection.iterator(), UnagiProjectProjectTreeElement.class);
		List<UnagiProject> selectedProjects = new ArrayList<UnagiProject>();
		for (UnagiProjectProjectTreeElement elem : selectedElements)
			selectedProjects.add(elem.getProject());

		// Adds the "Close projects" action to the list of applicable actions.
		actionList.add(new CloseProjectsAction(selectedProjects));
		
		return actionList;
	}

	/** @see java.lang.Comparable#compareTo(java.lang.Object) */
	@Override
	public int compareTo(ProjectTreeElement o) {
		// Check for nulls and different classes.
		if ((o == null) || (! (o instanceof UnagiProjectProjectTreeElement))) 
			return -1;
		
		// Compare by project.
		UnagiProjectProjectTreeElement e = (UnagiProjectProjectTreeElement) o;
		return (project == null) ? 1 : project.compareTo(e.project);
	}

	/** @see java.lang.Object#equals(java.lang.Object) */
	@Override
	public boolean equals(Object o) {
		// Check for nulls and different classes.
		if ((o == null) || (! (o instanceof UnagiProjectProjectTreeElement))) 
			return false;

		// If the elements refer to the same project, they're equal.
		UnagiProjectProjectTreeElement e = (UnagiProjectProjectTreeElement) o;
		return (project == null) ? false : project.equals(e.project);
	}

	/** @see java.lang.Object#hashCode() */
	@Override
	public int hashCode() {
		// Uses the project hash code.
		return (project == null) ? 0 : project.hashCode();
	}
}
