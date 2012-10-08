package it.unitn.disi.util.logging;


/**
 * A logger implementation that does nothing. All methods are implemented with empty bodies.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public final class EmptyLogger implements ILogger {
	/** @see it.unitn.disi.zanshin.util.ILogger#debug(java.lang.String, java.lang.Object[]) */
	@Override
	public void debug(String message, Object ... params) {}

	/** @see it.unitn.disi.zanshin.util.ILogger#info(java.lang.String, java.lang.Object[]) */
	@Override
	public void info(String message, Object ... params) {}

	/** @see it.unitn.disi.zanshin.util.ILogger#warn(java.lang.String, java.lang.Object[]) */
	@Override
	public void warn(String message, Object ... params) {}

	/** @see it.unitn.disi.zanshin.util.ILogger#error(java.lang.String, java.lang.Object[]) */
	@Override
	public void error(String message, Object ... params) {}

	/** @see it.unitn.disi.zanshin.util.ILogger#debug(java.lang.String, java.lang.Throwable, java.lang.Object[]) */
	@Override
	public void debug(String message, Throwable error, Object ... params) {}

	/** @see it.unitn.disi.zanshin.util.ILogger#info(java.lang.String, java.lang.Throwable, java.lang.Object[]) */
	@Override
	public void info(String message, Throwable error, Object ... params) {}

	/** @see it.unitn.disi.zanshin.util.ILogger#warn(java.lang.String, java.lang.Throwable, java.lang.Object[]) */
	@Override
	public void warn(String message, Throwable error, Object ... params) {}

	/** @see it.unitn.disi.zanshin.util.ILogger#error(java.lang.String, java.lang.Throwable, java.lang.Object[]) */
	@Override
	public void error(String message, Throwable error, Object ... params) {}
}
