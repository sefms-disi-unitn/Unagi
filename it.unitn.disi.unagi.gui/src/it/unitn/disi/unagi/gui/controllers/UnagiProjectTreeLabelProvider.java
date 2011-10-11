package it.unitn.disi.unagi.gui.controllers;

import it.unitn.disi.unagi.gui.models.ProjectTreeElement;
import it.unitn.disi.unagi.gui.nls.Messages;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * Class that provides labels for all elements of the Projects tree. Label and icon are computed depending on the type
 * of element.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class UnagiProjectTreeLabelProvider extends LabelProvider {
	/** @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object) */
	@Override
	public String getText(Object element) {
		// If the element belongs to the Project Tree model, ask it for its label.
		if (element instanceof ProjectTreeElement)
			return ((ProjectTreeElement) element).getLabel();

		// Otherwise it's an unknown element...
		return Messages.getString("gui.controller.unagiProjectTreeLabelProvider.unknownElement"); //$NON-NLS-1$
	}

	/** @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object) */
	@Override
	public Image getImage(Object element) {
		// If the element belongs to the Project Tree model, ask it for its icon.
		if (element instanceof ProjectTreeElement)
			return ((ProjectTreeElement) element).getIcon();

		// Otherwise it's an unknown element. Fall back to the shared object icon from Eclipse.
		return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
	}
}
