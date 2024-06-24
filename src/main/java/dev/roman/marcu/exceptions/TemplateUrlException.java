package dev.roman.marcu.exceptions;

public class TemplateUrlException extends RuntimeException {
	public TemplateUrlException(final Exception e) {
		super(e);
	}

	public TemplateUrlException(final String msg) {
		super(msg);
	}
}
