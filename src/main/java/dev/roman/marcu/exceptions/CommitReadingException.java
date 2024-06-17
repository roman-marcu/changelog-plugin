package dev.roman.marcu.exceptions;

public class CommitReadingException extends RuntimeException {
	public CommitReadingException(final Exception e) {
		super(e);
	}

	public CommitReadingException(final String message) {
		super(message);
	}
}
