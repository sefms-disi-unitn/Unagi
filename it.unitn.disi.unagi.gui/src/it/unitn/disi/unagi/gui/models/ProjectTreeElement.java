package it.unitn.disi.unagi.gui.models;

import it.unitn.disi.unagi.gui.nls.Messages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * A Project Tree Element (or PT Element) is a GUI model object that represents a domain entity that is being shown in
 * the project tree that displays all open projects in the tool. This abstract class declares and defines default
 * implementations for methods that provide information that is needed by the GUI's controller to display projects and
 * their children in the tree.
 * 
 * Although it's not mandatory, it's advised that all elements that should be displayed in the project tree be a
 * subclass of this class. Having this GUI model hierarchy lowers the amount of code (in particular, big if-else-if...
 * structures and switch statements) in the GUI controllers (UnagiProjectTreeContentProvider,
 * UnagiProjectTreeLabelProvider, ...).
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public abstract class ProjectTreeElement implements Comparable<ProjectTreeElement> {
	/**
	 * Provides the label of the element that is to be displayed at the Project Tree.
	 * 
	 * @return A string representing the element in textual form.
	 */
	public String getLabel() {
		// Returns a default label. Subclasses should override this method.
		return Messages.getString("gui.controller.unagiProjectTreeLabelProvider.unknownElement"); //$NON-NLS-1$
	}

	/**
	 * Provides the icon of the element that is to be displayed at the Project Tree.
	 * 
	 * @return An Image object representing the element in an iconic form.
	 */
	public Image getIcon() {
		// By default, return one of Eclipse's icons that represents a generic object.
		return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
	}

	/**
	 * Indicates to the Project Tree controller if the element has children.
	 * 
	 * @return <code>true</code> if the element has children, <code>false</code> otherwise.
	 */
	public boolean hasChildren() {
		// By default, elements do not have children.
		return false;
	}

	/**
	 * Provides the children of an element of the tree (supposedly other Project Tree Elements).
	 * 
	 * @return An array of objects that are to be displayed in the project tree by the controller.
	 */
	public Object[] getChildren() {
		// By default, elements do not have children.
		return null;
	}

	/**
	 * Provides the parent of an element of the tree (supposedly another Project Tree Element).
	 * 
	 * @return An object that represents the parent of the current element in the project tree.
	 */
	public Object getParent() {
		// By default, elements do not have a parent.
		return null;
	}

	/**
	 * Provides a list of applicable actions that can be applied to the element of the tree. This method is called during
	 * the creation of the dynamic context menu that appears when an element of the tree is right-clicked.
	 * 
	 * @param selection
	 *          The current selected items in the tree.
	 * @return A List of IAction objects, each of which represents an applicable action that will be displayed in the
	 *         element's context menu.
	 */
	public List<IAction> getApplicableActions(IStructuredSelection selection) {
		// By default, there are no applicable actions.
		return new ArrayList<IAction>();
	}
}
