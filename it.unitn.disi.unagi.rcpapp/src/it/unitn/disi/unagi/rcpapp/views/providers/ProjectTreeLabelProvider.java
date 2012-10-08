package it.unitn.disi.unagi.rcpapp.views.providers;

import it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement;
import it.unitn.disi.util.logging.LogUtil;

import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Label provider for the project tree. This class is responsible for telling Eclipse which icon and which text should
 * appear in each element of the tree. It does this by delegating this task to each model element that composes the
 * tree.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
@Creatable
@Singleton
public class ProjectTreeLabelProvider extends LabelProvider {
	/** @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object) */
	@Override
	public String getText(Object element) {
		// Check for nulls.
		if (element == null)
			return null;

		// If the supplied argument is a model object, delegate.
		if (element instanceof AbstractProjectTreeElement)
			return ((AbstractProjectTreeElement) element).getText();

		// Otherwise, return its string representation as default.
		LogUtil.log.warn("Unrecognizable type for the project tree contents (getText()): {0}.", element.getClass().getCanonicalName()); //$NON-NLS-1$
		return "" + element; //$NON-NLS-1$
	}

	/** @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object) */
	@Override
	public Image getImage(Object element) {
		// Check for nulls.
		if (element == null)
			return null;

		// If the supplied argument is a model object, delegate.
		if (element instanceof AbstractProjectTreeElement)
			return ((AbstractProjectTreeElement) element).getImage();

		// Otherwise, return nothing.
		LogUtil.log.warn("Unrecognizable type for the project tree contents (getImage()): {0}.", element.getClass().getCanonicalName()); //$NON-NLS-1$
		return null;
	}
}
