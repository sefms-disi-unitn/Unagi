package it.unitn.disi.unagi.gui.models;

import it.unitn.disi.unagi.application.util.CollectionsUtil;
import it.unitn.disi.unagi.domain.core.RequirementsModel;
import it.unitn.disi.unagi.gui.actions.DeleteRequirementsModelsAction;
import it.unitn.disi.unagi.gui.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;

/**
 * Project Tree element that represents a model of a Unagi Project.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class ModelPTElement extends ProjectTreeElement {
	/** The parent of a model is one of the Unagi Project's children (of the "Models" category). */
	private UnagiProjectChildPTElement parent;

	/** The model domain entity this PT element refers to. */
	private RequirementsModel model;

	/** Constructor. */
	public ModelPTElement(UnagiProjectChildPTElement parent, RequirementsModel model) {
		this.parent = parent;
		this.model = model;
	}

	/** @see it.unitn.disi.unagi.gui.models.ProjectTreeElement#getParent() */
	@Override
	public UnagiProjectChildPTElement getParent() {
		return parent;
	}

	/** Getter for model. */
	public RequirementsModel getModel() {
		return model;
	}

	/** @see it.unitn.disi.unagi.gui.models.ProjectTreeElement#getLabel() */
	@Override
	public String getLabel() {
		// Use the model name as label.
		return model.getName();
	}

	/** @see it.unitn.disi.unagi.gui.models.ProjectTreeElement#getIcon() */
	@Override
	public Image getIcon() {
		// Use the same image for all models.
		return ImageUtil.loadImage("/icons/entity-unagiproject-models-requirements.png"); //$NON-NLS-1$
	}

	/** @see it.unitn.disi.unagi.gui.models.ProjectTreeElement#getApplicableActions(org.eclipse.jface.viewers.IStructuredSelection) */
	@Override
	public List<IAction> getApplicableActions(IStructuredSelection selection) {
		List<IAction> actionList = new ArrayList<IAction>();
		
		// Builds a list of all selected models for the actions that can be applied to multiple elements.
		List<ModelPTElement> selectedElements = CollectionsUtil.filterSelectionByClass(selection.iterator(), ModelPTElement.class);
		List<RequirementsModel> selectedModels = new ArrayList<RequirementsModel>();
		for (ModelPTElement elem : selectedElements)
			selectedModels.add(elem.getModel());

		// Adds the "Delete requirements models" action to the list of applicable actions.
		actionList.add(new DeleteRequirementsModelsAction(selectedModels));
		
		return actionList;
	}

	/** @see java.lang.Comparable#compareTo(java.lang.Object) */
	@Override
	public int compareTo(ProjectTreeElement o) {
		// Check for nulls and different classes.
		if ((o == null) || (! (o instanceof ModelPTElement))) 
			return -1;
		
		// Compare by project.
		ModelPTElement e = (ModelPTElement) o;
		return (model == null) ? 1 : model.compareTo(e.model);
	}

	/** @see java.lang.Object#equals(java.lang.Object) */
	@Override
	public boolean equals(Object o) {
		// Check for nulls and different classes.
		if ((o == null) || (! (o instanceof ModelPTElement))) 
			return false;

		// If the elements refer to the same model, they're equal.
		ModelPTElement e = (ModelPTElement) o;
		return (model == null) ? false : model.equals(e.model);
	}

	/** @see java.lang.Object#hashCode() */
	@Override
	public int hashCode() {
		// Uses the model's hash code.
		return (model == null) ? 0 : model.hashCode();
	}
}
