package org.mule.extension.circuit.breaker.internal.exceptions;

/**
 * Exception indicating that the Circuit Breaker has been opened
 * @author Patryk <patryk.bandurski@gmail.com>
 *
 */
public class OpenedCicuitException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public OpenedCicuitException() {
		super("Circuit has been opened.");
	}
}
