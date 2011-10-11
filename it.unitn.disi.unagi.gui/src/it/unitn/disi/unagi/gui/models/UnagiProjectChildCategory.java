package it.unitn.disi.unagi.gui.models;

import it.unitn.disi.unagi.gui.nls.Messages;

/**
 * Enumeration of the different categories for the children of an Unagi Project, such as "Models", "Classes", etc. The
 * actual contents of the project will be divided in these categories when shown in the GUI (e.g., the projects tree).
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public enum UnagiProjectChildCategory {
	/** EMF models that represent the requirements of the system. */
	MODELS(Messages.getString("gui.model.unagiProjectChildCategory.enum.models"), "/icons/entity-unagiproject-models.png"), //$NON-NLS-1$ //$NON-NLS-2$

	/** Classes that implement the EMF models. */
	CLASSES(Messages.getString("gui.model.unagiProjectChildCategory.enum.classes"), "/icons/entity-unagiproject-classes.png"), //$NON-NLS-1$ //$NON-NLS-2$

	/** OCL(tm) constraints on the models (e.g., Awareness Requirements). */
	CONSTRAINTS(Messages.getString("gui.model.unagiProjectChildCategory.enum.constraints"), "/icons/entity-unagiproject-constraints.png"), //$NON-NLS-1$ //$NON-NLS-2$

	/** Rules compiled from the models and constraints. */
	RULES(Messages.getString("gui.model.unagiProjectChildCategory.enum.rules"), "/icons/entity-unagiproject-rules.png"), //$NON-NLS-1$ //$NON-NLS-2$

	/** Classes that implement simulations that test the monitoring. */
	SIMULATION(Messages.getString("gui.model.unagiProjectChildCategory.enum.simulation"), "/icons/entity-unagiproject-simulation.png"); //$NON-NLS-1$ //$NON-NLS-2$

	/** Label that represents this category. */
	private final String label;

	/** Path to the icon that represents this category. */
	private final String iconPath;

	/** Constructor. */
	private UnagiProjectChildCategory(String label, String iconPath) {
		this.label = label;
		this.iconPath = iconPath;
	}

	/** Getter for property: label. */
	public String getLabel() {
		return label;
	}

	/** Getter for property: iconPath. */
	public String getIconPath() {
		return iconPath;
	}
}
