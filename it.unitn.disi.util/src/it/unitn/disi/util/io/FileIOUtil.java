package it.unitn.disi.util.io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Utility class that operates on files.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class FileIOUtil {
	/**
	 * Special character used to declare variables in a template file. Note that the same character should be present in
	 * either the prefix or the suffix of a variable declaration.
	 * 
	 * @see it.unitn.disi.util.io.FileIOUtil.VARIABLE_DECLARATION_REGEX_PREFIX
	 * @see it.unitn.disi.util.io.FileIOUtil.VARIABLE_DECLARATION_REGEX_SUFFIX
	 */
	private static final char VARIABLE_DECLARATION_CHAR = '$';

	/** Prefix for a variable declaration in a template file (in regular expression sintax). */
	private static final String VARIABLE_DECLARATION_REGEX_PREFIX = "\\$\\{"; //$NON-NLS-1$

	/** Suffix for a variable declaration in a template file (in regular expression sintax). */
	private static final String VARIABLE_DECLARATION_REGEX_SUFFIX = "\\}"; //$NON-NLS-1$

	/**
	 * Utility method that reads a file given its URL and returns its contents.
	 * 
	 * Using URLs should be the preferred way of reading files, given that files that are retrieved from bundles cannot be
	 * read directly by a file reader. This method obtains an input stream by opening a connection with the URL and,
	 * therefore, is more generic and can handle files from within the workspace and the plug-in bundles.
	 * 
	 * @param url
	 *          The URL of the file whose contents should be read.
	 * @return The contents of the file whose URL was given.
	 * @throws IOException
	 *           If there are any I/O problems reading the contents of the file from its URL.
	 */
	public static StringBuffer readFile(URL url) throws IOException {
		StringBuffer stringBuffer = new StringBuffer();

		// Opens an input stream using the given URL.
		InputStream inputStream = url.openConnection().getInputStream();

		// Reads from the input stream into the string buffer and returns.
		performRead(new InputStreamReader(inputStream), stringBuffer);
		return stringBuffer;
	}

	/**
	 * Internal method that performs the actual reading of contents, given an open reader (which could be a file reader,
	 * input stream reader, string reader, etc.). This method also closes the reader when finished.
	 * 
	 * @param reader
	 *          The reader that allows us to access the contents.
	 * @param stringBuffer
	 *          The string buffer in which to place the contents read.
	 * @throws IOException
	 *           If there are any I/O problems using the given reader.
	 */
	private static void performRead(Reader reader, StringBuffer stringBuffer) throws IOException {
		// Defines a 1KB buffer to use in the reading.
		char[] buffer = new char[1024];

		// Reads the contents one buffer at a time.
		for (;;) {
			int charsRead = reader.read(buffer);
			if (charsRead == -1)
				break;
			stringBuffer.append(buffer, 0, charsRead);
		}

		// Closes the reader.
		reader.close();
	}

	/**
	 * Utility method that saves some contents into a file, given its URI.
	 * 
	 * Notice that this method doesn't use URLs as the case with file reading because the URLs pointing at workspace files
	 * don't support output streams. Therefore, writing is done using normal file writes and, thus, can only be used in
	 * files that exist in the local file system.
	 * 
	 * @param uri
	 *          The URI of the file in which to write the contents.
	 * @param contents
	 *          The contents that should be written in the file.
	 * @throws IOException
	 *           If there are any I/O problems writing the contents to the given file.
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

	/**
	 * Processes a file that contains the template for the initial contents of some other file, replacing the variables
	 * present in such contents with values provided in a map that associates the variable's names to their values.
	 * 
	 * @param url
	 *          The URL of the template file that should be read.
	 * @param map
	 *          The map that associates variables to their values.
	 * @return A string with the contents of the template file after variable substitution.
	 * @throws IOException
	 *           If there are any I/O problems reading the contents of the template file.
	 */
	public static String processTemplate(URL url, Map<String, Object> map) throws IOException {
		StringBuilder builder = new StringBuilder();

		// Reads the file into a buffer.
		StringBuffer buffer = readFile(url);

		// Reads the buffer line by line.
		Scanner scanner = new Scanner(buffer.toString());
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();

			// Translates the given map to one that uses regular expressions for variables as keys. This is done so the
			// concatenation and the toString() are not repeated for each line that contains a variable declaration char.
			Map<String, String> varMap = new HashMap<>();
			for (Map.Entry<String, Object> entry : map.entrySet())
				varMap.put(VARIABLE_DECLARATION_REGEX_PREFIX + entry.getKey() + VARIABLE_DECLARATION_REGEX_SUFFIX, entry.getValue().toString());

			// Looks for variables in the line and replace them with their values.
			if (line.contains(Character.toString(VARIABLE_DECLARATION_CHAR))) {
				for (Map.Entry<String, String> entry : varMap.entrySet()) {
					line = line.replaceAll(entry.getKey(), entry.getValue());
				}
			}

			// Adds the line to the builder.
			builder.append(line).append('\n');
		}

		// Closes the scanner and returns the built string.
		scanner.close();
		return builder.toString();
	}
}
