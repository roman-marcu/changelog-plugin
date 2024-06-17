package dev.roman.marcu.git;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;

import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.jupiter.api.Test;

import dev.roman.marcu.ConventionalCommit;

class ConventionalCommitParserTest extends GitRepositoryTest {

	@Test
	void testParseCommit_WithFeatureAndDescription() throws Exception {
		final RevCommit commit = buildCommit("""
				feat: test1
				""");
		final ConventionalCommit parsedCommit = new GitCommitParser().buildCommit(commit).get();
		assertEquals("feat", parsedCommit.getType());
		assertEquals("test1", parsedCommit.getDescription());
		assertEquals(false, parsedCommit.isBreakingChange());
		assertNull(parsedCommit.getScope(), "Scope should be null");
		assertTrue(parsedCommit.getBody().isEmpty(), "Body should be empty");
		assertTrue(parsedCommit.getFooters().isEmpty(), "Footers should be empty");
	}

	@Test
	void testParseCommit_WithBreakingChangeOnFirstLine() throws Exception {
		final RevCommit commit = buildCommit("""
				feat!: test1
				""");
		final ConventionalCommit parsedCommit = new GitCommitParser().buildCommit(commit).get();
		assertEquals("feat", parsedCommit.getType());
		assertEquals("test1", parsedCommit.getDescription());
		assertEquals(true, parsedCommit.isBreakingChange());
	}

	@Test
	void testParseCommit_WithScopeAndBreakingChangeOnFirstLine() throws Exception {
		final RevCommit commit = buildCommit("""
				feat(test)!: test1
				""");
		final ConventionalCommit parsedCommit = new GitCommitParser().buildCommit(commit).get();
		assertEquals("feat", parsedCommit.getType());
		assertEquals("test1", parsedCommit.getDescription());
		assertEquals(true, parsedCommit.isBreakingChange());
		assertEquals("test", parsedCommit.getScope());
	}

	@Test
	void testParseCommit_WithScope() throws Exception {
		final RevCommit commit = buildCommit("""
				feat(test): test1
				""");
		final ConventionalCommit parsedCommit = new GitCommitParser().buildCommit(commit).get();
		assertEquals("feat", parsedCommit.getType());
		assertEquals("test1", parsedCommit.getDescription());
		assertEquals(false, parsedCommit.isBreakingChange());
		assertEquals("test", parsedCommit.getScope());
	}

	@Test
	void testParseCommit_WithBodyAndFooter() throws Exception {
		final RevCommit commit = buildCommit("""
				feat(test)!: test1

				Description of the body

				footer-1: footer1

				""");
		final ConventionalCommit parsedCommit = new GitCommitParser().buildCommit(commit).get();
		assertEquals("feat", parsedCommit.getType());
		assertEquals("test1", parsedCommit.getDescription());
		assertEquals(true, parsedCommit.isBreakingChange());
		assertEquals("test", parsedCommit.getScope());
		assertEquals(Collections.singletonMap("footer-1", "footer1"), parsedCommit.getFooters());
	}

	@Test
	void testParseCommit_WithBreakingChangeFooter() throws Exception {
		final RevCommit commit = buildCommit("""
				feat(test): test1

				BREAKING-CHANGE: change that will affect all apps

				""");
		final ConventionalCommit parsedCommit = new GitCommitParser().buildCommit(commit).get();
		assertEquals("feat", parsedCommit.getType());
		assertEquals("test1", parsedCommit.getDescription());
		assertEquals(true, parsedCommit.isBreakingChange());
		assertEquals("test", parsedCommit.getScope());
		assertEquals(1, parsedCommit.getFooters().size());
		assertEquals(Collections.singleton(
						new AbstractMap.SimpleEntry("BREAKING-CHANGE", "change that will affect all apps")),
				parsedCommit.getFooters().entrySet());
	}

	@Test
	void testParseCommit_WithFooters() throws Exception {
		final RevCommit commit = buildCommit("""
				feat(test)!: test1

				footer-1: footer1
				footer-2: footer2
				footer-3: footer3
				""");
		final ConventionalCommit parsedCommit = new GitCommitParser().buildCommit(commit).get();
		assertEquals("feat", parsedCommit.getType());
		assertEquals("test1", parsedCommit.getDescription());
		assertEquals(true, parsedCommit.isBreakingChange());
		assertEquals("test", parsedCommit.getScope());
		assertEquals(true, parsedCommit.getBody().isEmpty());
		assertEquals(new HashMap<>(3) {{
			put("footer-1", "footer1");
			put("footer-2", "footer2");
			put("footer-3", "footer3");
		}}, parsedCommit.getFooters());
	}

	@Test
	void testParseCommit_Description() throws Exception {
		final RevCommit commit = buildCommit("""
				feat(test): test1

				test1
								
				""");
		final String description = GitCommitParser.getDescription(commit.getFullMessage());
		assertEquals("test1", description);
	}

	@Test
	void testParseCommit_NoDescription() throws Exception {
		final RevCommit commit = buildCommit("""
				feat(test): test1
				""");
		final String description = GitCommitParser.getDescription(commit.getFullMessage());
		assertEquals("", description);
	}

	@Test
	void testParseCommit_DescriptionWithFooter() throws Exception {
		final RevCommit commit = buildCommit("""
				feat(test): test1

				test1
								
				footer-1: footer1
				""");
		final String description = GitCommitParser.getDescription(commit.getFullMessage());
		assertEquals("test1", description);
	}
}