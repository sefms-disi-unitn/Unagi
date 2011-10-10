package it.unitn.disi.unagi.domain.core;

import it.unitn.disi.util.domain.DomainObjectSupport;

import java.io.File;

/**
 * Domain class that represents requirements models that belong to Unagi projects.
 * 
 * The requirements model is an EMF ecore file, based on other EMF ecore files provided by the tool. In practice this
 * allows the developer to describe a goal model as the requirements of the adaptive system. Awareness Requirements and
 * other adaptivity requirements can then be provided on top of this goal model.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class RequirementsModel extends DomainObjectSupport implements Comparable<RequirementsModel> {
	/** Version UID for serialization purposes. */
	private static final long serialVersionUID = 5995208944728935582L;

	/** Project to which the requirements model belongs. */
	private UnagiProject project;

	/** File that contains the requirements model (e.g., an EMF Ecore file). */
	private File file;

	/** Name of the model. */
	private String name;

	/** Base package for classes generated from this model. */
	private String basePackage;

	/** Constructor. */
	public RequirementsModel(File file, String name) {
		this.file = file;
		this.name = name;
	}

	/** Getter for project. */
	public UnagiProject getProject() {
		return project;
	}

	/** Setter for project. */
	public void setProject(UnagiProject project) {
		this.project = project;
	}

	/** Getter for property: file. */
	public File getFile() {
		return file;
	}

	/** Setter for property: file. */
	public void setFile(File file) {
		this.file = file;
	}

	/** Getter for property: name. */
	public String getName() {
		return name;
	}

	/** Setter for property: name. */
	public void setName(String name) {
		this.name = name;
	}

	/** Getter for basePackage. */
	public String getBasePackage() {
		return basePackage;
	}

	/** Setter for basePackage. */
	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	/** @see java.lang.Comparable#compareTo(java.lang.Object) */
	@Override
	public int compareTo(RequirementsModel o) {
		int cmp = 0;

		// Performs null checks and compares by project.
		if ((o == null) || (o.project == null))
			cmp = -1;
		else if (project == null)
			cmp = 1;
		else cmp = project.compareTo(o.project);
		if (cmp != 0)
			return cmp;

		// In case of tie, performs null checks and compares by requirements model name.
		if ((o == null) || (o.name == null))
			cmp = -1;
		else if (name == null)
			cmp = 1;
		else cmp = name.compareTo(o.name);

		// In case of tie, compare by the superclass' criteria.
		if (cmp == 0)
			cmp = super.compareTo(o);
		return cmp;
	}
}
