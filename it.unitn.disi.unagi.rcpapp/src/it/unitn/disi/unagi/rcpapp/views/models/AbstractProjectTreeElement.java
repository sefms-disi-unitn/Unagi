package it.unitn.disi.unagi.rcpapp.views.models;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

/**
 * Abstract class that represents any element of the Unagi project tree. This class defines the methods that should be
 * overridden by concrete project tree model elements, providing default (mostly empty) implementations for them in
 * order to avoid obligating the subclass to overwrite methods that are not related to the concrete implementation.
 * 
 * The definition of standard methods to be implemented by every member of the project tree has the purpose of allowing
 * the project tree content and label providers to delegate to the tree elements themselves the task of providing the
 * tree contents and labels, thus allowing for this logic to be distributed among different objects instead of
 * concentrating everything in the providers, using huge switch-case/if-then-else structures.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public abstract class AbstractProjectTreeElement {
	/** Base prefix for pop-up menus in the RCP Application. */
	protected static final String POPUP_MENU_PREFIX = "it.unitn.disi.unagi.rcpapp.popupmenu.projectExplorer."; //$NON-NLS-1$

	/** Prefix for the default (empty) pop-up menu, shown for all elements unless specifically overridden. */
	protected static final String DEFAULT_POPUP_MENU_ID = POPUP_MENU_PREFIX + "default"; //$NON-NLS-1$

	/** Bundle object that represents the RCP Application. Necessary for image loading. */
	protected Bundle bundle;

	/** Constructor. */
	protected AbstractProjectTreeElement(Bundle bundle) {
		this.bundle = bundle;
	}

	/**
	 * Returns the Unagi project under which the element is located.
	 * 
	 * This method has no default implementation and has to be overwritten by every concrete model class.
	 * 
	 * @return The Unagi project under which the element is located.
	 */
	public abstract IProject getProject();

	/**
	 * Returns the children of this element.
	 * 
	 * By default, returns <code>null</code>, assuming it's a leaf element (no children).
	 * 
	 * @return An array containing the children of this element.
	 */
	public Object[] getChildren() {
		return null;
	}

	/**
	 * Returns the parent of this element.
	 * 
	 * By default, returns <code>null</code>, assuming it's the root element (no parent).
	 * 
	 * @return The parent of this element.
	 */
	public Object getParent() {
		return null;
	}

	/**
	 * Indicates if this element has children.
	 * 
	 * By default, returns <code>false</code>, assuming it's a leaf element (no children).
	 * 
	 * @return <code>true</code>, if the element has children, <code>false</code> otherwise.
	 */
	public boolean hasChildren() {
		return false;
	}

	/**
	 * Returns the text that should be displayed by the element in the tree.
	 * 
	 * By default, returns the element's conversion to String (toString()).
	 * 
	 * @return The text that should be displayed by the element in the tree.
	 */
	public String getText() {
		return toString();
	}

	/**
	 * Returns the image that should be displayed by the element in the tree.
	 * 
	 * By default, returns <code>null</code> (no image).
	 * 
	 * @return The image that should be displayed by the element in the tree.
	 */
	public Image getImage() {
		return null;
	}

	/**
	 * Returns the ID of the pop-up menu that should be displayed when this element is right-clicked.
	 * 
	 * By default, returns the ID of the default pop-up menu, which is empty.
	 * 
	 * @return The ID of this element's pop-up menu.
	 */
	public String getPopupMenuId() {
		return DEFAULT_POPUP_MENU_ID;
	}

	/**
	 * Returns the ID of the default command for this class of elements. This command is executed when an element of this
	 * kind is double-clicked.
	 * 
	 * By default, returns null. Whoever calls this method has to check for nulls and cancel execution.
	 * 
	 * @return The ID of this element's default command.
	 */
	public String getDefaultCommandId() {
		return null;
	}
}
