package dev.roman.marcu.git;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashMap;

import org.eclipse.jgit.lib.CheckoutEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.ReflogEntry;
import org.junit.jupiter.api.Test;

class ConventionalCommitParserTest {

	@Test
	void testParseCommit_WithFeatureAndDescription() {
		final String comment = """
				feat: test1
				""";
		final ReflogEntry commit = new ReflogEntryStub(comment);
		final ConventionalCommit parsedCommit = ConventionalCommitParser.parseCommit(commit);
		assertEquals("feat", parsedCommit.type());
		assertEquals("test1", parsedCommit.description());
		assertEquals(false, parsedCommit.isBreakingChange());
		assertNull(parsedCommit.scope(), "Scope should be null");
		assertTrue(parsedCommit.body().isEmpty(), "Body should be empty");
		assertTrue(parsedCommit.footers().isEmpty(), "Footers should be empty");
	}

	@Test
	void testParseCommit_WithBreakingChangeOnFirstLine() {
		final String comment = """
				feat!: test1
				""";
		final ReflogEntry commit = new ReflogEntryStub(comment);
		final ConventionalCommit parsedCommit = ConventionalCommitParser.parseCommit(commit);
		assertEquals("feat", parsedCommit.type());
		assertEquals("test1", parsedCommit.description());
		assertEquals(true, parsedCommit.isBreakingChange());
	}

	@Test
	void testParseCommit_WithScopeAndBreakingChangeOnFirstLine() {
		final String comment = """
				feat(test)!: test1
				""";
		final ReflogEntry commit = new ReflogEntryStub(comment);
		final ConventionalCommit parsedCommit = ConventionalCommitParser.parseCommit(commit);
		assertEquals("feat", parsedCommit.type());
		assertEquals("test1", parsedCommit.description());
		assertEquals(true, parsedCommit.isBreakingChange());
		assertEquals("test", parsedCommit.scope());
	}

	@Test
	void testParseCommit_WithScope() {
		final String comment = """
				feat(test): test1
				""";
		final ReflogEntry commit = new ReflogEntryStub(comment);
		final ConventionalCommit parsedCommit = ConventionalCommitParser.parseCommit(commit);
		assertEquals("feat", parsedCommit.type());
		assertEquals("test1", parsedCommit.description());
		assertEquals(false, parsedCommit.isBreakingChange());
		assertEquals("test", parsedCommit.scope());
	}

	@Test
	void testParseCommit_WithBody() {
		final String comment = """
				feat(test)!: test1
								
				Description of the body				
				""";
		final ReflogEntry commit = new ReflogEntryStub(comment);
		final ConventionalCommit parsedCommit = ConventionalCommitParser.parseCommit(commit);
		assertEquals("feat", parsedCommit.type());
		assertEquals("test1", parsedCommit.description());
		assertEquals(true, parsedCommit.isBreakingChange());
		assertEquals("test", parsedCommit.scope());
		assertEquals("Description of the body", parsedCommit.body());
	}

	@Test
	void testParseCommit_WithMultiLinesBody() {
		final String comment = """
				feat(test)!: test1
								
				Description of the body	
								
				On multilines	
						
				""";
		final ReflogEntry commit = new ReflogEntryStub(comment);
		final ConventionalCommit parsedCommit = ConventionalCommitParser.parseCommit(commit);
		assertEquals("feat", parsedCommit.type());
		assertEquals("test1", parsedCommit.description());
		assertEquals(true, parsedCommit.isBreakingChange());
		assertEquals("test", parsedCommit.scope());
		assertEquals("Description of the body\n\nOn multilines", parsedCommit.body());
	}

	@Test
	void testParseCommit_WithBodyAndFooter() {
		final String comment = """
				feat(test)!: test1
								
				Description of the body	
								
				footer-1: footer1	
						
				""";
		final ReflogEntry commit = new ReflogEntryStub(comment);
		final ConventionalCommit parsedCommit = ConventionalCommitParser.parseCommit(commit);
		assertEquals("feat", parsedCommit.type());
		assertEquals("test1", parsedCommit.description());
		assertEquals(true, parsedCommit.isBreakingChange());
		assertEquals("test", parsedCommit.scope());
		assertEquals("Description of the body", parsedCommit.body());
		assertEquals(Collections.singletonMap("footer-1", "footer1"), parsedCommit.footers());
	}

	@Test
	void testParseCommit_WithBreakingChangeFooter() {
		final String comment = """
				feat(test): test1
								
				Description of the body	
								
				BREAKING CHANGE: change that will affect all apps.	
						
				""";
		final ReflogEntry commit = new ReflogEntryStub(comment);
		final ConventionalCommit parsedCommit = ConventionalCommitParser.parseCommit(commit);
		assertEquals("feat", parsedCommit.type());
		assertEquals("test1", parsedCommit.description());
		assertEquals(true, parsedCommit.isBreakingChange());
		assertEquals("test", parsedCommit.scope());
		assertEquals("Description of the body", parsedCommit.body());
		assertEquals(Collections.singletonMap("BREAKING CHANGE", "change that will affect all apps."),
				parsedCommit.footers());
	}

	@Test
	void testParseCommit_WithFooters() {
		final String comment = """
				feat(test)!: test1
								
								
				footer-1: footer1
				footer-2: footer2
				footer-3: footer3						
				""";
		final ReflogEntry commit = new ReflogEntryStub(comment);
		final ConventionalCommit parsedCommit = ConventionalCommitParser.parseCommit(commit);
		assertEquals("feat", parsedCommit.type());
		assertEquals("test1", parsedCommit.description());
		assertEquals(true, parsedCommit.isBreakingChange());
		assertEquals("test", parsedCommit.scope());
		assertEquals("", parsedCommit.body());
		assertEquals(new HashMap<>(3) {{
			put("footer-1", "footer1");
			put("footer-2", "footer2");
			put("footer-3", "footer3");
		}}, parsedCommit.footers());
	}

	class ReflogEntryStub implements ReflogEntry {

		private String comment;

		public ReflogEntryStub(final String comment) {
			this.comment = comment;
		}

		@Override
		public String getComment() {
			return comment;
		}

		@Override
		public ObjectId getOldId() {
			return null;
		}

		@Override
		public ObjectId getNewId() {
			return null;
		}

		@Override
		public PersonIdent getWho() {
			return null;
		}

		@Override
		public CheckoutEntry parseCheckout() {
			return null;
		}
	}
}