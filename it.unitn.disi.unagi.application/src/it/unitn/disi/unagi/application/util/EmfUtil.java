package it.unitn.disi.unagi.application.util;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Utility class that operates on EMF models.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class EmfUtil {
	/**
	 * TODO: document this method.
	 * 
	 * @param resourceSet
	 * @param ecorePath
	 * @return
	 */
	public static EPackage retrieveEPackage(ResourceSet resourceSet, IPath ecorePath) {
		URI ecoreURI = URI.createFileURI(ecorePath.toString());
		Resource resource = resourceSet.getResource(ecoreURI, true);
		return (EPackage) resource.getContents().get(0);
	}

	/**
	 * TODO: document this method.
	 * 
	 * @param ecorePath
	 * @param ePackage
	 * @return
	 */
	public static GenModel createGenModel(IPath ecorePath, EPackage ePackage, IPath modelDir) throws IOException {
		GenModel genModel = GenModelFactory.eINSTANCE.createGenModel();
		genModel.setModelDirectory(modelDir.toString());
		genModel.getForeignModel().add(ecorePath.toString());
		genModel.initialize(Collections.singleton(ePackage));
		genModel.setModelName(ecorePath.removeFileExtension().lastSegment());

		return genModel;
	}
}
