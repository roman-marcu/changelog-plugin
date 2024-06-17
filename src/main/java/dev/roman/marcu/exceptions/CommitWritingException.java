package dev.roman.marcu.exceptions;

public class CommitWritingException extends RuntimeException {
	public CommitWritingException(final Exception e) {
		super(e);
	}
}
