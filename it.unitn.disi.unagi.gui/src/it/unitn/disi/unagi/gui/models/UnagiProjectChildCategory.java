package it.unitn.disi.unagi.gui.models;

/**
 * Enumeration of the different categories for the children of an Unagi Project, such as "Models", "Classes", etc. The
 * actual contents of the project will be divided in these categories when shown in the GUI (e.g., the projects tree).
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public enum UnagiProjectChildCategory {
	/** EMF models that represent the requirements of the system. */
	MODELS("Models", "/icons/entity-unagiproject-models.png"),

	/** Classes that implement the EMF models. */
	CLASSES("Classes", "/icons/entity-unagiproject-classes.png"),

	/** OCL(tm) constraints on the models (e.g., Awareness Requirements). */
	CONSTRAINTS("Constraints", "/icons/entity-unagiproject-constraints.png"),

	/** Rules compiled from the models and constraints. */
	RULES("Rules", "/icons/entity-unagiproject-rules.png"),

	/** Classes that implement simulations that test the monitoring. */
	SIMULATION("Simulation", "/icons/entity-unagiproject-simulation.png");

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
