package it.unitn.disi.unagi.rcpapp.views.providers;

import it.unitn.disi.unagi.rcpapp.IUnagiRcpAppBundleInfoProvider;
import it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement;
import it.unitn.disi.unagi.rcpapp.views.models.ProjectProjectTreeElement;
import it.unitn.disi.util.logging.LogUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Content provider for the project tree. This class is responsible for telling Eclipse what are the elements that exist
 * in the tree. It does this by delegating questions such as: does this element have children? which are they? what is
 * the element's parent? to each model element that composes the tree.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
@Creatable
@Singleton
public class ProjectTreeContentProvider implements ITreeContentProvider {
	/** The bundle's activator, used to retrieve global information on the RCP application. */
	@Inject
	private IUnagiRcpAppBundleInfoProvider activator;

	/** @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object) */
	@Override
	public Object[] getElements(Object inputElement) {
		// If the input is null, the tree is empty.
		if (inputElement == null)
			return null;

		// If the input is an array of project tree elements, that is already the list of elements. Return it.
		if (inputElement instanceof AbstractProjectTreeElement[]) {
			LogUtil.log.debug("Obtaining project tree contents: returning the already created project tree model."); //$NON-NLS-1$
			return (AbstractProjectTreeElement[]) inputElement;
		}

		// If the input is an array of projects, create the array of project tree elements from the open projects.
		if (inputElement instanceof IProject[]) {
			IProject[] projects = (IProject[]) inputElement;
			LogUtil.log.debug("Obtaining project tree contents: converting the array of {0} projects into an array of project tree elements representing these projects.", projects.length); //$NON-NLS-1$
			List<AbstractProjectTreeElement> elems = new ArrayList<>();
			for (int i = 0; i < projects.length; i++) {
				IProject project = projects[i];
				if (project.isOpen())
					elems.add(new ProjectProjectTreeElement(activator.getBundle(), project));
			}
			return elems.toArray();
		}

		// Otherwise, the input is not recognized, therefore return nothing.
		LogUtil.log.warn("Unrecognizable type for the project tree contents (getElements()): {0}.", inputElement.getClass().getCanonicalName()); //$NON-NLS-1$
		return null;
	}

	/** @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object) */
	@Override
	public Object[] getChildren(Object parentElement) {
		// If the supplied argument is a model object, delegate.
		if (parentElement instanceof AbstractProjectTreeElement)
			return ((AbstractProjectTreeElement) parentElement).getChildren();

		// Otherwise, return nothing.
		LogUtil.log.warn("Unrecognizable type for the project tree contents (getChildren()): {0}.", parentElement.getClass().getCanonicalName()); //$NON-NLS-1$
		return null;
	}

	/** @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object) */
	@Override
	public Object getParent(Object element) {
		// If the supplied argument is a model object, delegate.
		if (element instanceof AbstractProjectTreeElement)
			return ((AbstractProjectTreeElement) element).getParent();

		// Otherwise, return nothing.
		LogUtil.log.warn("Unrecognizable type for the project tree contents (getParent()): {0}.", element.getClass().getCanonicalName()); //$NON-NLS-1$
		return null;
	}

	/** @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object) */
	@Override
	public boolean hasChildren(Object element) {
		// If the supplied argument is a model object, delegate.
		if (element instanceof AbstractProjectTreeElement)
			return ((AbstractProjectTreeElement) element).hasChildren();

		// Otherwise, consider it has no children.
		LogUtil.log.warn("Unrecognizable type for the project tree contents (hasChildren()): {0}.", element.getClass().getCanonicalName()); //$NON-NLS-1$
		return false;
	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// No need to respond to changes.
	}

	/** @see org.eclipse.jface.viewers.IContentProvider#dispose() */
	@Override
	public void dispose() {}
}
