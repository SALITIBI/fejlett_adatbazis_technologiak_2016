package hu.unideb.inf.universe.exception;

public class UniverseException extends Exception {

	private static final long serialVersionUID = 1L;

	public UniverseException() {
		super();
	}

	public UniverseException(String message) {
		super(message);
	}

	public UniverseException(Throwable cause) {
		super(cause);
	}

	public UniverseException(String message, Throwable cause) {
		super(message, cause);
	}

	public UniverseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
