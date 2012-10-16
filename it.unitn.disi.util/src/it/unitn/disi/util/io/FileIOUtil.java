package it.unitn.disi.util.io;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

/**
 * TODO: document this type.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class FileIOUtil {
	/**
	 * TODO: document this method.
	 * 
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	public static StringBuffer readFile(String uri) throws IOException {
		FileReader reader = null;
		StringBuffer stringBuffer = new StringBuffer();

		// Opens a file reader for the given file.
		reader = new FileReader(uri);

		// Reads the file's contents one KB at a time and add it to a string buffer.
		char[] buffer = new char[1024];
		for (;;) {
			int charsRead = reader.read(buffer);
			if (charsRead == -1)
				break;
			stringBuffer.append(buffer, 0, charsRead);
		}

		// Closes the reader.
		reader.close();

		// Returns the contents of the string buffer.
		return stringBuffer;
	}

	/**
	 * TODO: document this method.
	 * 
	 * @param uri
	 * @param contents
	 * @throws IOException
	 */
	public static void saveFile(String uri, String contents) throws IOException {
		// Creates a string reader to read the contents little by little and also a file writer.
		StringReader reader = new StringReader(contents);
		FileWriter writer = new FileWriter(uri);

		// Reads the contents one KB at a time and write it to the file.
		char[] buffer = new char[1024];
		for (;;) {
			int charsRead = reader.read(buffer);
			if (charsRead == -1)
				break;
			writer.write(buffer, 0, charsRead);
		}

		// Closes the reader and the writer.
		reader.close();
		writer.close();
	}
}
