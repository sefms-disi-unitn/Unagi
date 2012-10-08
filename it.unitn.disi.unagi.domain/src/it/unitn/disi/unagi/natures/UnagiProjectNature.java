package it.unitn.disi.unagi.natures;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

/**
 * Defines the nature of a Unagi Project.
 *
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class UnagiProjectNature implements IProjectNature {
	/** The nature's identifier. */
	public static final String NATURE_ID = "it.unitn.disi.unagi.natures.project"; //$NON-NLS-1$
	
	/** The project to which this nature applies. */
	private IProject project;
	
	/** @see org.eclipse.core.resources.IProjectNature#configure() */
	@Override
	public void configure() throws CoreException { }

	/** @see org.eclipse.core.resources.IProjectNature#deconfigure() */
	@Override
	public void deconfigure() throws CoreException { }

	/** @see org.eclipse.core.resources.IProjectNature#getProject() */
	@Override
	public IProject getProject() {
		return project;
	}

	/** @see org.eclipse.core.resources.IProjectNature#setProject(org.eclipse.core.resources.IProject) */
	@Override
	public void setProject(IProject project) {
		this.project = project;
	}
}
