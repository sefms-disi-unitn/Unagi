package it.unitn.disi.unagi.application.persistence;

import it.unitn.disi.unagi.domain.core.UnagiProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Implementation of a persistence manager for the class UnagiProject that uses Java serialization for persistence.
 *
 * @see it.unitn.disi.unagi.application.persistence.UnagiProjectPersistenceManager
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class SerializationUnagiProjectPersistenceManager implements UnagiProjectPersistenceManager {
	/** Name of the file where the project is serialized. */
	private static final String PROJECT_FILE = "project.unagi";
	
	/** @see it.unitn.disi.unagi.application.persistence.UnagiProjectPersistenceManager#save(it.unitn.disi.unagi.domain.core.UnagiProject, java.io.File) */
	@Override
	public void save(UnagiProject project, File folder) throws IOException {
		ObjectOutputStream out = null;
		
		// Checks if the specified folder already exists and is a folder.
		if (folder.exists() && (! folder.isDirectory()))
			throw new IOException("Specified folder \"" + folder.getAbsolutePath() + "\" is not a directory.");
		
		// If it doesn't yet exist, create it.
		else if (! folder.exists()) folder.mkdir();
		
		// Serializes the project to a file in the given folder.
		try {
			out = new ObjectOutputStream(new FileOutputStream(new File(folder, PROJECT_FILE)));
			out.writeObject(project);
		}
		finally {
			if (out != null) out.close();
		}
	}

	/** @see it.unitn.disi.unagi.application.persistence.UnagiProjectPersistenceManager#checkProjectFolder(java.io.File) */
	@Override
	public boolean checkProjectFolder(File folder) {
		// Checks if the provided folder exists and is indeed a directory.
		if (! (folder.exists() && folder.isDirectory())) return false;
		
		// If passed the first check, checks if the project file is present in the folder.
		File projectFile = new File(folder, PROJECT_FILE);
		return projectFile.exists() && projectFile.isFile();
	}

	/** @see it.unitn.disi.unagi.application.persistence.UnagiProjectPersistenceManager#load(java.io.File) */
	@Override
	public UnagiProject load(File folder) throws ClassNotFoundException, FileNotFoundException, IOException {
		ObjectInputStream in = null;
		UnagiProject project = null;
		
		// Loads the project from its serialized version contained in the file.
		try {
			in = new ObjectInputStream(new FileInputStream(new File(folder, PROJECT_FILE)));
			project = (UnagiProject)in.readObject();
		}
		finally {
			if (in != null) in.close();
		}
		
		return project;
	}
}
