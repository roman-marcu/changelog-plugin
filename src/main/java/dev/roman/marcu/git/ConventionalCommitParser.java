package dev.roman.marcu.git;

import org.eclipse.jgit.lib.ReflogEntry;

public class ConventionalCommitParser {
	private ConventionalCommitParser(){}
	public static final String IS_BREAKING_CHANGE_MARK_ON_FIRST_LINE = "!";
	public static final String BREAKING_CHANGE_MARK_IN_FOOTER = "BREAKING-CHANGE";
	public static final String SCOPE_START_CHARACTER = "(";
	public static final String SCOPE_END_CHARACTER = ")";
	public static final String SPLIT_ON_TYPE_AND_DESCRIPTION = ": ";
	public static final String SPLIT_ON_FOOTER_BREAKING_CHANGE1 = ": ";
	public static final String SPLIT_ON_FOOTER_BREAKING_CHANGE2 = " #";
	private static final String END_OF_LINE = "\n";

	/***
	 * <type>[optional scope]: <description>
	 *
	 * [optional body]
	 *
	 * [optional footer(s)]
	 * @param reflogEntry
	 * @return
	 */
	public static ConventionalCommit parseCommit(final ReflogEntry reflogEntry) {
		String comment = reflogEntry.getComment();

		final int firstLineIndex = comment.indexOf(END_OF_LINE);
		final ConventionalCommit.Builder builder = parseCommitFirstLine(comment.substring(0, firstLineIndex));
		comment = comment.substring(firstLineIndex + 1);
		int indexOfFooter = getNextFooterIndex(comment, 0);
		if (indexOfFooter > 0) {
			int lastEOL = comment.substring(0, indexOfFooter).lastIndexOf(END_OF_LINE);
			builder.setBody(comment.substring(0, lastEOL).trim());
			comment = comment.substring(lastEOL + 1);
			while (indexOfFooter > 0) {
				indexOfFooter = getNextFooterIndex(comment, 0);
				String value;
				String key = comment.substring(0, indexOfFooter);
				int nextIndexOfFooter = getNextFooterIndex(comment, indexOfFooter + 1);
				if (nextIndexOfFooter > 0) {
					lastEOL = comment.substring(0, nextIndexOfFooter).lastIndexOf(END_OF_LINE);
					value = comment.substring(indexOfFooter + 1, lastEOL);
					comment = comment.substring(lastEOL + 1);
				} else {
					value = comment.substring(indexOfFooter + 1);
					indexOfFooter = -1;
				}
				if (isBreakingChangeFooter(key.trim()))
					builder.setBreakingChange(true);
				builder.addFooter(key.trim(), value.trim());
			}
		} else {
			builder.setBody(comment.trim());
		}
		return builder.build();
	}

	private static boolean isBreakingChangeFooter(final String footerKey) {
		return BREAKING_CHANGE_MARK_IN_FOOTER.equalsIgnoreCase(footerKey.replace(" ", "-"));
	}

	private static int getNextFooterIndex(final String comment, final int startIndex) {
		int indexOfFooter = comment.indexOf(SPLIT_ON_FOOTER_BREAKING_CHANGE1, startIndex);
		if (indexOfFooter <= 0)
			indexOfFooter = comment.indexOf(SPLIT_ON_FOOTER_BREAKING_CHANGE2, startIndex);
		return indexOfFooter;
	}

	private static ConventionalCommit.Builder parseCommitFirstLine(final String line) {
		int index = line.indexOf(SPLIT_ON_TYPE_AND_DESCRIPTION);
		if (index < 1) {
			throw new RuntimeException("Incorrect format of first line");
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
		return type.indexOf(IS_BREAKING_CHANGE_MARK_ON_FIRST_LINE) > 0;
	}

}
