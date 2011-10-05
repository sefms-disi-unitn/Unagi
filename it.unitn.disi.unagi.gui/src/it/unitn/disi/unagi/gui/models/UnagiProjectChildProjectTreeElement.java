package it.unitn.disi.unagi.gui.models;

import it.unitn.disi.unagi.domain.core.RequirementsModel;
import it.unitn.disi.unagi.domain.core.UnagiProject;
import it.unitn.disi.unagi.gui.actions.NewRequirementsModelAction;
import it.unitn.disi.unagi.gui.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;

/**
 * In the GUI model for the projects tree, represents the direct children of a Unagi Project, which just separates the
 * project's real contents in categories, such as "Models", "Classes", "Constraints", etc.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class UnagiProjectChildProjectTreeElement extends ProjectTreeElement {
	/** Project that this child refers to. */
	private UnagiProject parent;

	/** Category of the child (models, classes, constraints, etc.). */
	private UnagiProjectChildCategory category;

	/** Constructor. */
	public UnagiProjectChildProjectTreeElement(UnagiProject project, UnagiProjectChildCategory category) {
		this.parent = project;
		this.category = category;
	}

	/**
	 * Getter for parent.
	 * 
	 * @see it.unitn.disi.unagi.gui.models.ProjectTreeElement#getParent()
	 */
	@Override
	public UnagiProject getParent() {
		return parent;
	}

	/** Getter for category. */
	public UnagiProjectChildCategory getCategory() {
		return category;
	}

	/** @see it.unitn.disi.unagi.gui.models.ProjectTreeElement#getLabel() */
	@Override
	public String getLabel() {
		// Use the category as label.
		return category.getLabel();
	}

	/** @see it.unitn.disi.unagi.gui.models.ProjectTreeElement#getIcon() */
	@Override
	public Image getIcon() {
		// Use the category's icon.
		return ImageUtil.loadImage(category.getIconPath());
	}

	/** @see it.unitn.disi.unagi.gui.models.ProjectTreeElement#hasChildren() */
	@Override
	public boolean hasChildren() {
		// Project children have children of their own (they are used to separate the project's elements actually).
		return true;
	}

	/** @see it.unitn.disi.unagi.gui.models.ProjectTreeElement#getChildren() */
	@Override
	public Object[] getChildren() {
		// Each child shows a different set of project elements.
		switch (category) {
		case MODELS:
			List<RequirementsModel> models = new ArrayList<RequirementsModel>(parent.getRequirementsModels());
			ModelProjectTreeElement[] children = new ModelProjectTreeElement[models.size()];
			for (int i = 0; i < children.length; i++) children[i] = new ModelProjectTreeElement(this, models.get(i));
			return children;
		}
		
		// If it's a different category from the ones above, use the default behavior.
		return super.getChildren();
	}

	/** @see it.unitn.disi.unagi.gui.models.ProjectTreeElement#getApplicableActions(org.eclipse.jface.viewers.IStructuredSelection) */
	@Override
	public List<IAction> getApplicableActions(IStructuredSelection selection) {
		List<IAction> actionList = new ArrayList<IAction>();
		boolean singleSelection = (selection.size() == 1);

		// Each child has a different context menu. Add the applicable actions to the list.
		switch (category) {
		case MODELS:
			// Single-element action: "New requirements model".
			if (singleSelection) actionList.add(new NewRequirementsModelAction(parent));
		}
		
		return actionList;
	}

	/** @see java.lang.Comparable#compareTo(java.lang.Object) */
	@Override
	public int compareTo(ProjectTreeElement o) {
		// Check for nulls and different classes.
		if ((o == null) || (! (o instanceof UnagiProjectChildProjectTreeElement))) 
			return -1;
		
		// Compare by project.
		UnagiProjectChildProjectTreeElement e = (UnagiProjectChildProjectTreeElement) o;
		return (category == null) ? 1 : category.compareTo(e.category);
	}

	/** @see java.lang.Object#equals(java.lang.Object) */
	@Override
	public boolean equals(Object o) {
		// Check for nulls and different classes.
		if ((o == null) || (! (o instanceof UnagiProjectChildProjectTreeElement))) 
			return false;

		// If the elements refer to the same project and child category, they're equal.
		UnagiProjectChildProjectTreeElement e = (UnagiProjectChildProjectTreeElement) o;
		return ((parent == null) || (category == null)) ? false : (parent.equals(e.parent) && category.equals(e.category));
	}

	/** @see java.lang.Object#hashCode() */
	@Override
	public int hashCode() {
		// Uses the project and the category's hash code.
		return ((parent == null) || (category == null)) ? 0 : (parent.hashCode() + category.hashCode());
	}
}
