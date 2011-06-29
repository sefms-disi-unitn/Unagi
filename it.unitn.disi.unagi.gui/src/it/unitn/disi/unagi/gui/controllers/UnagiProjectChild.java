package it.unitn.disi.unagi.gui.controllers;

import it.unitn.disi.unagi.domain.core.UnagiProject;

/**
 * In the GUI model for the projects tree, represents the direct children of a Unagi Project, which just separates the
 * project's real contents in categories, such as "Models", "Classes", "Constraints", etc.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class UnagiProjectChild {
	/** Project that this child refers to. */
	private UnagiProject parent;

	/** Category of the child (models, classes, constraints, etc.). */
	private UnagiProjectChildCategory category;

	/** Constructor. */
	public UnagiProjectChild(UnagiProject project, UnagiProjectChildCategory category) {
		this.parent = project;
		this.category = category;
	}

	/** Getter for property: project. */
	public UnagiProject getParent() {
		return parent;
	}

	/** Getter for property: category. */
	public UnagiProjectChildCategory getCategory() {
		return category;
	}
}
