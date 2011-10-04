package it.unitn.disi.unagi.gui.controllers;

import it.unitn.disi.unagi.domain.core.UnagiProject;
import it.unitn.disi.unagi.gui.utils.ImageUtil;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * Class that provides labels for all elements of the Projects tree. Label and
 * icon are computed depending on the type of element.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class UnagiProjectLabelProvider extends LabelProvider {
	/** @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object) */
	@Override
	public String getText(Object element) {
		// Checks if the element is a Unagi Project.
		if (element instanceof UnagiProject)
			return ((UnagiProject) element).getName();

		// Check if it's one of the Project's direct children.
		else if (element instanceof UnagiProjectChild)
			return ((UnagiProjectChild) element).getCategory().getLabel();

		// Otherwise it's an unknown element...
		return "(Unknown element)";
	}

	/** @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object) */
	@Override
	public Image getImage(Object element) {
		// Determines the path from which the icon is loaded depending on the
		// class of the element.
		String iconPath = null;
		if (element instanceof UnagiProject)
			iconPath = "/icons/entity-unagiproject.png";
		else if (element instanceof UnagiProjectChild)
			iconPath = ((UnagiProjectChild) element).getCategory()
					.getIconPath();

		// If there's an icon path, tries to load the image from the path.
		Image icon = null;
		if (iconPath != null)
			icon = ImageUtil.loadImage(iconPath);

		// If no icon was found, fall back to the shared object icon from
		// Eclipse.
		if (icon == null)
			icon = PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_FILE);

		return icon;
	}
}
