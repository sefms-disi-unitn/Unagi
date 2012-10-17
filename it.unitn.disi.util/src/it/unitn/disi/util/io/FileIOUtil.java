package it.unitn.disi.util.io;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * TODO: document this type.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class FileIOUtil {
	/** TODO: document this field. */
	private static final char VARIABLE_DECLARATION_CHAR = '$';

	/** TODO: document this field. */
	private static final String VARIABLE_DECLARATION_REGEX_PREFIX = "\\$\\{"; //$NON-NLS-1$

	/** TODO: document this field. */
	private static final String VARIABLE_DECLARATION_REGEX_SUFFIX = "\\}"; //$NON-NLS-1$

	/**
	 * TODO: document this method.
	 * 
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	public static StringBuffer readFile(String uri) throws IOException {
		// FIXME: can't we use only the readPlatformFile() version? Check uses of this one...
		
		StringBuffer stringBuffer = new StringBuffer();
		char[] buffer = new char[1024];

		// Reads the file's contents one KB at a time and add it to a string buffer.
		performRead(new FileReader(uri), buffer, stringBuffer);

		// Returns the contents of the string buffer.
		return stringBuffer;
	}

	/**
	 * TODO: document this method.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static StringBuffer readPlatformFile(URL url) throws IOException {
		// Uses a string buffer as result and a char buffer for reading.
		StringBuffer stringBuffer = new StringBuffer();
		char[] buffer = new char[1024];

		// Opens an input stream using the given URL.
		InputStream inputStream = url.openConnection().getInputStream();

		// Reads from the input stream into the string buffer and returns.
		performRead(new InputStreamReader(inputStream), buffer, stringBuffer);
		return stringBuffer;
	}

	/**
	 * TODO: document this method.
	 * 
	 * @param reader
	 * @param buffer
	 * @param stringBuffer
	 * @throws IOException
	 */
	private static void performRead(Reader reader, char[] buffer, StringBuffer stringBuffer) throws IOException {
		// Reads the contents of the reader one buffer at a time.
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

	/**
	 * TODO: document this method.
	 * 
	 * @param uri
	 * @param map
	 * @return
	 * @throws IOException
	 */
	public static String processTemplate(URL url, Map<String, Object> map) throws IOException {
		StringBuilder builder = new StringBuilder();

		// Reads the file into a buffer.
		StringBuffer buffer = readPlatformFile(url);

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
