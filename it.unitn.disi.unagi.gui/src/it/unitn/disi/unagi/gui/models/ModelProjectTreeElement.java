package it.unitn.disi.unagi.gui.models;

import it.unitn.disi.unagi.domain.core.RequirementsModel;
import it.unitn.disi.unagi.gui.utils.ImageUtil;

import org.eclipse.swt.graphics.Image;

/**
 * TODO: document this type.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class ModelProjectTreeElement extends ProjectTreeElement {
	/** Project child that this element refers to. */
	private UnagiProjectChildProjectTreeElement parent;

	/** The model itself. */
	private RequirementsModel model;

	/** Constructor. */
	public ModelProjectTreeElement(UnagiProjectChildProjectTreeElement parent, RequirementsModel model) {
		this.parent = parent;
		this.model = model;
	}

	/**
	 * Getter for parent.
	 * 
	 * @see it.unitn.disi.unagi.gui.models.ProjectTreeElement#getParent()
	 */
	@Override
	public UnagiProjectChildProjectTreeElement getParent() {
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
		return ImageUtil.loadImage("/icons/entity-unagiproject-models-requirements.png");
	}

	/** @see java.lang.Comparable#compareTo(java.lang.Object) */
	@Override
	public int compareTo(ProjectTreeElement o) {
		// Check for nulls and different classes.
		if ((o == null) || (! (o instanceof ModelProjectTreeElement))) 
			return -1;
		
		// Compare by project.
		ModelProjectTreeElement e = (ModelProjectTreeElement) o;
		return (model == null) ? 1 : model.compareTo(e.model);
	}

	/** @see java.lang.Object#equals(java.lang.Object) */
	@Override
	public boolean equals(Object o) {
		// Check for nulls and different classes.
		if ((o == null) || (! (o instanceof ModelProjectTreeElement))) 
			return false;

		// If the elements refer to the same model, they're equal.
		ModelProjectTreeElement e = (ModelProjectTreeElement) o;
		return (model == null) ? false : model.equals(e.model);
	}

	/** @see java.lang.Object#hashCode() */
	@Override
	public int hashCode() {
		// Uses the model's hash code.
		return (model == null) ? 0 : model.hashCode();
	}
}
