package dev.roman.marcu.git;

import java.util.Optional;

import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.roman.marcu.ConventionalCommit;
import dev.roman.marcu.parser.ConventionalCommitParser;

public class GitCommitParser extends ConventionalCommitParser<RevCommit> {
	final Logger logger = LoggerFactory.getLogger(GitCommitParser.class);

	private static boolean hasBreakingChangeMarkInFooter(final RevCommit revCommit) {
		return !revCommit.getFooterLines(BREAKING_CHANGE_MARK_IN_FOOTER1).isEmpty()
					   || !revCommit.getFooterLines(BREAKING_CHANGE_MARK_IN_FOOTER2).isEmpty();
	}

	@Override
	public Optional<ConventionalCommit> buildCommit(final RevCommit revCommit) {
		try {
			final ConventionalCommit.Builder builder = parseCommitFirstLine(revCommit.getShortMessage());
			revCommit.getFooterLines().forEach(f -> builder.addFooter(f.getKey(), f.getValue()));
			if (hasBreakingChangeMarkInFooter(revCommit))
				builder.setBreakingChange(true);
			builder.setBody(getDescription(revCommit.getFullMessage()));
			return Optional.of(builder.build());
		} catch (Exception e) {
			logger.debug("Could not parse commit: {}", revCommit.getShortMessage(), e);
			return Optional.empty();
		}
	}
}
