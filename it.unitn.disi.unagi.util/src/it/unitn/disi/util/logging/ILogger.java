package it.unitn.disi.util.logging;


/**
 * Interface for a logger utility used by Zanshin components.
 * 
 * Zanshin's Core Component provides an empty logger implementation that is used by default by all Zanshin components
 * and a logger implementation that uses OSGi's logging platform that is used by Zanshin components if they detect that
 * Zanshin's logging component has already been registered in the platform.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 * @see it.unitn.disi.zanshin.util.EmptyLogger
 * @see it.unitn.disi.zanshin.util.PlatformLogger
 */
public interface ILogger {
	/**
	 * Logs a debug message.
	 * 
	 * @param message
	 *          Message to log.
	 */
	public abstract void debug(String message, Object ... params);

	/**
	 * Logs an informational message.
	 * 
	 * @param message
	 *          Message to log.
	 */
	public abstract void info(String message, Object ... params);

	/**
	 * Logs a warning message.
	 * 
	 * @param message
	 *          Message to log.
	 */
	public abstract void warn(String message, Object ... params);

	/**
	 * Logs an error message.
	 * 
	 * @param message
	 *          Message to log.
	 */
	public abstract void error(String message, Object ... params);

	/**
	 * Logs a debug message with an associated error.
	 * 
	 * @param message
	 *          Message to log.
	 * @param error
	 *          Associated error.
	 */
	public abstract void debug(String message, Throwable error, Object ... params);

	/**
	 * Logs an informational message with an associated error.
	 * 
	 * @param message
	 *          Message to log.
	 * @param error
	 *          Associated error.
	 */
	public abstract void info(String message, Throwable error, Object ... params);

	/**
	 * Logs a warning message with an associated error.
	 * 
	 * @param message
	 *          Message to log.
	 * @param error
	 *          Associated error.
	 */
	public abstract void warn(String message, Throwable error, Object ... params);

	/**
	 * Logs an error message with an associated error.
	 * 
	 * @param message
	 *          Message to log.
	 * @param error
	 *          Associated error.
	 */
	public abstract void error(String message, Throwable error, Object ... params);

}
