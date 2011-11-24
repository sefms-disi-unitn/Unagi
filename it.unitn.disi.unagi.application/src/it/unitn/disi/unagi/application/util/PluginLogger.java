package it.unitn.disi.unagi.application.util;

import java.text.MessageFormat;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;

/**
 * A logger for Eclipse plug-ins, offering shortcuts to the most used logging operations (info, warning, error).
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class PluginLogger {
	/** The eclipse logger. */
	private ILog logger;

	/** The ID of the plug-in that is sending the log messages. */
	private String pluginId;

	/** Constructor. */
	public PluginLogger(ILog logger, String pluginId) {
		this.logger = logger;
		this.pluginId = pluginId;
	}

	/**
	 * Protected method that actually performs the logging. This method is called by the public log shortcut methods, thus
	 * centralizing the way logging is actually performed in one single method.
	 * 
	 * @param severity
	 *          The severity of the log message.
	 * @param message
	 *          The message to be logged.
	 * @param exception
	 *          The exception to be logged, if any.
	 */
	protected void doLog(int severity, String message, Throwable exception) {
		// Checks if an exception is to be logged.
		Status status = null;
		if (exception == null)
			status = new Status(severity, pluginId, message);
		else status = new Status(severity, pluginId, message, exception);

		// Sends the log message to Eclipse's log.
		logger.log(status);
	}

	/**
	 * Shortcut method for logging an informational message without any exceptions.
	 * 
	 * @param message
	 *          The message to be logged, which can use {0}, {1}, etc. as parameters placeholders, using
	 *          java.text.MessageFormat's syntax.
	 * @param params
	 *          Parameters to be merged in the log message, according to java.text.MessageFormat's syntax.
	 */
	public void info(String message, Object ... params) {
		doLog(Status.INFO, MessageFormat.format(message, params), null);
	}

	/**
	 * Shortcut method for logging an informational message with an exception.
	 * 
	 * @param e
	 *          The exception to be sent along the logged message.
	 * @param message
	 *          The message to be logged, which can use {0}, {1}, etc. as parameters placeholders, using
	 *          java.text.MessageFormat's syntax.
	 * @param params
	 *          Parameters to be merged in the log message, according to java.text.MessageFormat's syntax.
	 */
	public void info(Exception e, String message, Object ... params) {
		doLog(Status.INFO, MessageFormat.format(message, params), e);
	}

	/**
	 * Shortcut method for logging a warning message without any exceptions.
	 * 
	 * @param message
	 *          The message to be logged, which can use {0}, {1}, etc. as parameters placeholders, using
	 *          java.text.MessageFormat's syntax.
	 * @param params
	 *          Parameters to be merged in the log message, according to java.text.MessageFormat's syntax.
	 */
	public void warn(String message, Object ... params) {
		doLog(Status.WARNING, MessageFormat.format(message, params), null);
	}

	/**
	 * Shortcut method for logging a warning message with an exception.
	 * 
	 * @param e
	 *          The exception to be sent along the logged message.
	 * @param message
	 *          The message to be logged, which can use {0}, {1}, etc. as parameters placeholders, using
	 *          java.text.MessageFormat's syntax.
	 * @param params
	 *          Parameters to be merged in the log message, according to java.text.MessageFormat's syntax.
	 */
	public void warn(Exception e, String message, Object ... params) {
		doLog(Status.WARNING, MessageFormat.format(message, params), e);
	}

	/**
	 * Shortcut method for logging an error message without any exceptions.
	 * 
	 * @param message
	 *          The message to be logged, which can use {0}, {1}, etc. as parameters placeholders, using
	 *          java.text.MessageFormat's syntax.
	 * @param params
	 *          Parameters to be merged in the log message, according to java.text.MessageFormat's syntax.
	 */
	public void error(String message, Object ... params) {
		doLog(Status.ERROR, MessageFormat.format(message, params), null);
	}

	/**
	 * Shortcut method for logging an error message with an exception.
	 * 
	 * @param e
	 *          The exception to be sent along the logged message.
	 * @param message
	 *          The message to be logged, which can use {0}, {1}, etc. as parameters placeholders, using
	 *          java.text.MessageFormat's syntax.
	 * @param params
	 *          Parameters to be merged in the log message, according to java.text.MessageFormat's syntax.
	 */
	public void error(Exception e, String message, Object ... params) {
		doLog(Status.ERROR, MessageFormat.format(message, params), e);
	}
}
