package it.unitn.disi.util;




import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;

/**
 * This class is both a log listener and a service listener. As a log listener, it receives log entries and processes
 * them, formatting the log messages according to the specified patterns and printing them in the appropriate means. As
 * a service listener, it keeps track of all log reader services registered by OSGi in the platform and adds itself as a
 * listener to these services in order to be able to receive the log entries from other components in the first place.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class DisiLogService implements LogListener, ServiceListener {
	/** List that keeps track of all log reader services registered in the platform. */
	private List<LogReaderService> readers = new ArrayList<LogReaderService>();

	/** Date/time formatter for log entries. */
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$

	/** Message format for log entries. */
	private String messagePattern = "{0} [{1}] {2}: {3}"; //$NON-NLS-1$
	
	/** Minimum log level for messages to be displayed. */
	private int minLevel = LogService.LOG_DEBUG;

	/** Log level label array. */
	private String[] logLevelLabels = new String[] { "UNKNOWN", "ERROR", "WARNING", "INFO", "DEBUG" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	/** Changes the pattern for date format. */
	public void changeDatePattern(String datePattern) {
		dateFormat = new SimpleDateFormat(datePattern);
	}

	/** Changes the pattern for log messages. */
	public void changeMessagePattern(String messagePattern) {
		this.messagePattern = messagePattern;
	}
	
	/** Changes the minimum log level. */
	public void changeLevel(String levelName) {
		int minLevel = 0;
		for (int i = 0; minLevel == 0 && i < logLevelLabels.length; i++)
			if (logLevelLabels[i].equals(levelName))
				minLevel = i;
		if (minLevel == 0)
			printLogMessage(formatLogMessage(new Date(System.currentTimeMillis()), Activator.getBundleId(), this.minLevel, "Cannot change logging level. Unknown level: " + levelName)); //$NON-NLS-1$
		else this.minLevel = minLevel;
	}

	/** @see org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework.ServiceEvent) */
	@Override
	public void serviceChanged(ServiceEvent event) {
		// Keeps track of all log reader services registered in the platform.
		BundleContext bundleContext = event.getServiceReference().getBundle().getBundleContext();
		LogReaderService reader = (LogReaderService) bundleContext.getService(event.getServiceReference());
		if (reader != null) {
			// If a service is being registered, adds the DISI log listener to it.
			if (event.getType() == ServiceEvent.REGISTERED)
				addTo(reader);

			// If a service is being unregistered, removes the DISI log listener from it.
			else if (event.getType() == ServiceEvent.UNREGISTERING)
				removeFrom(reader);
		}
	}

	/**
	 * Adds this DISI log listener to the specified log reader service.
	 * 
	 * @param reader
	 *          The log reader service to which the listener should be added.
	 */
	public void addTo(LogReaderService reader) {
		readers.add(reader);
		reader.addLogListener(this);
	}

	/**
	 * Removes this DISI log listener from the specified log reader service.
	 * 
	 * @param reader
	 *          The log reader service from which the listener should be removed.
	 */
	public void removeFrom(LogReaderService reader) {
		reader.removeLogListener(this);
		readers.remove(reader);
	}

	/**
	 * Removes this DISI log listener from all the reader services to which it has been added.
	 */
	public void removeFromAll() {
		// Copies the list of readers to an array because lists cannot be changed while being iterated.
		LogReaderService[] readerArray = readers.toArray(new LogReaderService[0]);
		for (LogReaderService reader : readerArray)
			removeFrom(reader);
	}

	/** @see org.osgi.service.log.LogListener#logged(org.osgi.service.log.LogEntry) */
	@Override
	public void logged(LogEntry entry) {
		// Obtains the bundle name and message from the log entry.
		String bundleName = entry.getBundle().getSymbolicName();
		String message = entry.getMessage();

		// Only shows messages from DISI bundles that are at least at the minimum level.
		if ((message != null) && (entry.getLevel() <= minLevel) && bundleName.startsWith("it.unitn.disi")) { //$NON-NLS-1$
			// Formats and prints the message.
			String formattedMessage = formatLogMessage(new Date(entry.getTime()), bundleName, entry.getLevel(), entry.getMessage());
			printLogMessage(formattedMessage);
		}
	}

	/**
	 * Formats the log message according to the date and message patterns.
	 * 
	 * @param time
	 *          The date/time of the message.
	 * @param bundleName
	 *          The name of the bundle that produced the message.
	 * @param level
	 *          The logging level.
	 * @param message
	 *          The log message itself.
	 * @return The formatted log message.
	 */
	protected String formatLogMessage(Date time, String bundleName, int level, String message) {
		String formattedBundleName = (bundleName.substring(14) + "                            ").substring(0, 25); //$NON-NLS-1$
		return MessageFormat.format(messagePattern, dateFormat.format(time), formattedBundleName, logLevelLabels[level], message);
	}

	/**
	 * Prints the given message, using the appropriate means.
	 * 
	 * @param message
	 *          The message to be printed.
	 */
	protected void printLogMessage(String message) {
		// So far, this component can only handle standard output printing.
		System.out.println(message);
	}
}
