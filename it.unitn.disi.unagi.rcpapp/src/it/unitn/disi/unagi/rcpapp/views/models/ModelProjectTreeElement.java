package it.unitn.disi.unagi.rcpapp.views.models;

import org.osgi.framework.Bundle;

/**
 * Abstract superclass for elements that are shown under the models directory (requirements, constraints, etc.).
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public abstract class ModelProjectTreeElement extends AbstractProjectTreeElement {
	/** Constructor. */
	protected ModelProjectTreeElement(Bundle bundle) {
		super(bundle);
	}
}
