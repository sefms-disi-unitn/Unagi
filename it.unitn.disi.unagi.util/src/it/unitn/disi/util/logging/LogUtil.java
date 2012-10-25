package it.unitn.disi.util.logging;

import org.osgi.service.log.LogService;

/**
 * Utility class for logging.
 * 
 * This class offers a default empty logger if used outside of the Eclipse platform. When used in an RCP application, it
 * should get initialized with a proper logging service by the bundle's activator, after which the real logger is
 * offered. Such logger contains methods for each of the possible logging levels to facilitate its use.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public final class LogUtil {
	/** The logger. */
	public static ILogger log = new EmptyLogger();

	/** This class is not meant to have objects. */
	private LogUtil() {}

	/**
	 * Initialization method that should be called by the bundle's activator.
	 * 
	 * @param logService
	 *          The platform log service to be used for logging.
	 */
	public static void initialize(LogService logService) {
		log = new PlatformLogger(logService);
	}
}
