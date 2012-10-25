package it.unitn.disi.util.logging;


import java.text.MessageFormat;

import org.osgi.service.log.LogService;

/**
 * Logger implementation that uses the OSGi Platform logging services.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public final class PlatformLogger implements ILogger {
	/** The platform log service. */
	private LogService logService;

	/** Constructor. */
	public PlatformLogger(LogService logService) {
		this.logService = logService;
	}

	/** @see it.unitn.disi.zanshin.util.ILogger#debug(java.lang.String) */
	@Override
	public void debug(String message, Object ... params) {
		log(LogService.LOG_DEBUG, message, null, params);
	}

	/** @see it.unitn.disi.zanshin.util.ILogger#info(java.lang.String) */
	@Override
	public void info(String message, Object ... params) {
		log(LogService.LOG_INFO, message, null, params);
	}

	/** @see it.unitn.disi.zanshin.util.ILogger#warn(java.lang.String) */
	@Override
	public void warn(String message, Object ... params) {
		log(LogService.LOG_WARNING, message, null, params);
	}

	/** @see it.unitn.disi.zanshin.util.ILogger#error(java.lang.String) */
	@Override
	public void error(String message, Object ... params) {
		log(LogService.LOG_ERROR, message, null, params);
	}

	/** @see it.unitn.disi.zanshin.util.ILogger#debug(java.lang.String, java.lang.Throwable) */
	@Override
	public void debug(String message, Throwable error, Object ... params) {
		log(LogService.LOG_DEBUG, message, error, params);
	}

	/** @see it.unitn.disi.zanshin.util.ILogger#info(java.lang.String, java.lang.Throwable) */
	@Override
	public void info(String message, Throwable error, Object ... params) {
		log(LogService.LOG_INFO, message, error, params);
	}

	/** @see it.unitn.disi.zanshin.util.ILogger#warn(java.lang.String, java.lang.Throwable) */
	@Override
	public void warn(String message, Throwable error, Object ... params) {
		log(LogService.LOG_WARNING, message, error, params);
	}

	/** @see it.unitn.disi.zanshin.util.ILogger#error(java.lang.String, java.lang.Throwable) */
	@Override
	public void error(String message, Throwable error, Object ... params) {
		log(LogService.LOG_ERROR, message, error, params);
	}

	/**
	 * Internal method that processes logging messages from all levels.
	 * 
	 * @param level
	 *          The logging level.
	 * @param message
	 *          The message to log.
	 * @param error
	 *          The associated error.
	 * @param params
	 *          The message's parameters.
	 */
	private void log(int level, String message, Throwable error, Object ... params) {
		// Formats the message with the given parameters.
		String formattedMessage = MessageFormat.format(message, params);

		// Logs the message using the platform, checking if an error was provided or not.
		if (error == null)
			logService.log(level, formattedMessage);
		else logService.log(level, formattedMessage, error);
	}
}
