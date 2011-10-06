package it.unitn.disi.unagi.domain.core;

import it.unitn.disi.util.domain.DomainObjectSupport;

import java.io.File;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Domain class that represents Unagi Projects, one of the core concepts of the system.
 * 
 * Unagi Projects centralize the information related to the whole process of developing an adaptive application using
 * the Zanshin framework, both of which are part of the author's PhD research on requirements for adaptive systems. A
 * Unagi Project contains elements such as an EMF model of the requirements, classes implementing this model, OCL(tm)
 * constraints that represent Awareness Requirements on the system, Drool rules derived from these constraints, etc.
 * 
 * For more information, see the author's website for papers on the subject: http://disi.unitn.it/~vitorsouza/.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class UnagiProject extends DomainObjectSupport implements Comparable<UnagiProject> {
	/** Version UID for serialization purposes. */
	private static final long serialVersionUID = -2051532540784796911L;

	/** Folder in which the project is saved. */
	private File folder;

	/** Name of the project. */
	private String name;

	/** Timestamp of the last time the project was saved to file. */
	private Date saveTimestamp;
	
	/** Requirements models of this project. */
	private SortedSet<RequirementsModel> requirementsModels = new TreeSet<RequirementsModel>();

	/** Constructor. */
	public UnagiProject(File folder, String name) {
		this.folder = folder;
		this.name = name;
	}

	/** Getter for property: file. */
	public File getFolder() {
		return folder;
	}

	/** Setter for property: file. */
	public void setFolder(File folder) {
		this.folder = folder;
	}

	/** Getter for property: name. */
	public String getName() {
		return name;
	}

	/** Setter for property: name. */
	public void setName(String name) {
		this.name = name;
	}

	/** Getter for property: saveTimestamp. */
	public Date getSaveTimestamp() {
		return saveTimestamp;
	}

	/** Setter for property: saveTimestamp. */
	public void setSaveTimestamp(Date saveTimestamp) {
		this.saveTimestamp = saveTimestamp;
	}

	/**
	 * Updates the timestamp of the last save to the current date/time.
	 */
	public void updateSaveTimestamp() {
		saveTimestamp = new Date(System.currentTimeMillis());
	}

	/** Getter for requirementsModels. */
	public SortedSet<RequirementsModel> getRequirementsModels() {
		return requirementsModels;
	}

	/** Adds a requirements model to the project. */
	public void addRequirementsModel(RequirementsModel requirementsModel) {
		requirementsModel.setProject(this);
		requirementsModels.add(requirementsModel);
	}
	
	/** Removes a requirements model from the project. */
	public void removeRequirementsModel(RequirementsModel requirementsModel) {
		// Checks for illegal state: model to be removed does not exist in the project.
		if (! requirementsModels.contains(requirementsModel))
			throw new IllegalStateException();
		
		// Removes the model from the project.
		requirementsModels.remove(requirementsModel);
		requirementsModel.setProject(null);
	}
	
	/** @see java.lang.Comparable#compareTo(java.lang.Object) */
	@Override
	public int compareTo(UnagiProject o) {
		int cmp = 0;

		// Performs null checks and compares by project name.
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
