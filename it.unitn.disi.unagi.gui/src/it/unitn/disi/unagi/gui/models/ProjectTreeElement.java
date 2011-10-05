package it.unitn.disi.unagi.gui.models;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * TODO: document this type.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public abstract class ProjectTreeElement implements Comparable<ProjectTreeElement> {
	/**
	 * TODO: document this method.
	 * 
	 * @return
	 */
	public String getLabel() {
		// Returns a default label. Subclasses should override this method.
		return "(Unknown element)";
	}

	/**
	 * TODO: document this method.
	 * 
	 * @return
	 */
	public Image getIcon() {
		// By default, return one of Eclipse's icons that represents a generic object.
		return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
	}

	/**
	 * TODO: document this method.
	 * 
	 * @return
	 */
	public boolean hasChildren() {
		// By default, elements do not have children.
		return false;
	}

	/**
	 * TODO: document this method.
	 * 
	 * @return
	 */
	public Object[] getChildren() {
		// By default, elements do not have children.
		return null;
	}

	/**
	 * TODO: document this method.
	 * 
	 * @return
	 */
	public Object getParent() {
		// By default, elements do not have a parent.
		return null;
	}

	/**
	 * TODO: document this method.
	 * @param selection
	 * @return
	 */
	public List<IAction> getApplicableActions(IStructuredSelection selection) {
		// By default, there are no applicable actions.
		return new ArrayList<IAction>();
	}
}
