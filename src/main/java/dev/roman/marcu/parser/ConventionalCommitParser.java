package dev.roman.marcu.parser;

import java.util.Optional;

import dev.roman.marcu.ConventionalCommit;
import dev.roman.marcu.exceptions.IncorrectMessageFormatException;

public abstract class ConventionalCommitParser<T> {
	public static final String BREAKING_CHANGE_MARK_ON_FIRST_LINE = "!";
	public static final String BREAKING_CHANGE_MARK_IN_FOOTER1 = "BREAKING-CHANGE";
	public static final String BREAKING_CHANGE_MARK_IN_FOOTER2 = "BREAKING CHANGE";
	public static final String SCOPE_START_CHARACTER = "(";
	public static final String SCOPE_END_CHARACTER = ")";
	public static final String SPLIT_ON_TYPE_AND_DESCRIPTION = ": ";
	public static final int COMMIT_NEW_LINE = '\n';

	protected static ConventionalCommit.Builder parseCommitFirstLine(final String line) {
		int index = line.indexOf(SPLIT_ON_TYPE_AND_DESCRIPTION);
		if (index < 1) {
			throw new IncorrectMessageFormatException("Incorrect format of first line: " + line);
		}
		String type = line.substring(0, index);
		boolean isBreakingChange = hasBreakingChangeMarkInType(type);
		String scope = buildScope(type);
		if (scope != null) {
			type = type.substring(0, type.indexOf(scope) - 1);
		} else if (isBreakingChange) {
			type = type.substring(0, type.length() - 1);
		}
		return new ConventionalCommit.Builder(type, line.substring(index + 2)).setBreakingChange(isBreakingChange)
					   .setScope(scope);

	}

	private static String buildScope(final String type) {
		final int scopeIndex = type.indexOf(SCOPE_START_CHARACTER);
		if (scopeIndex > 0) {
			return type.substring(scopeIndex + 1, type.indexOf(SCOPE_END_CHARACTER)).trim();
		}
		return null;
	}

	private static boolean hasBreakingChangeMarkInType(final String type) {
		return type.indexOf(BREAKING_CHANGE_MARK_ON_FIRST_LINE) > 0;
	}

	public static String getDescription(final String fullMessage) {
		int firstLine = firstBlankLine(fullMessage, 0);
		if (firstLine != -1) {
			int lastLine = lastBlankLine(fullMessage, firstLine - 2);
			if (lastLine > firstLine)
				return fullMessage.substring(firstLine + 1, lastLine == -1 ? fullMessage.length() : lastLine);
		}
		return "";
	}

	private static int lastBlankLine(String msg, int start) {
		for (int i = msg.length() - 1; i > start; i--) {
			if (msg.charAt(i) == COMMIT_NEW_LINE && msg.charAt(i - 1) == COMMIT_NEW_LINE) {
				return i - 1;
			}
		}
		return -1;
	}

	private static int firstBlankLine(String msg, int start) {
		for (int i = start; i < msg.length() - 1; i++) {
			if (msg.charAt(i) == COMMIT_NEW_LINE && msg.charAt(i + 1) == COMMIT_NEW_LINE) {
				return i + 1;
			}
		}
		return -1;
	}

	public abstract Optional<ConventionalCommit> buildCommit(final T commit);
}
