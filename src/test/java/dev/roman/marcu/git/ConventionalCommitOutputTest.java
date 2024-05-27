package dev.roman.marcu.git;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import edu.emory.mathcs.backport.java.util.Collections;
import freemarker.template.TemplateException;

class ConventionalCommitOutputTest {

	@TempDir
	Path tempDir;

	@Test
	void testWrite() throws TemplateException, IOException, URISyntaxException {
		final ConventionalCommit commit = new ConventionalCommit("feat", false, "test", "Junit test", null, null);
		ConventionalCommitOutput.write(Collections.singletonList(commit), "CHANGELOG.md", tempDir.toString());

		assertEquals(true, Files.exists(Path.of(tempDir.toString(), "CHANGELOG.md")));
		final List<String> actualContent = Files.readAllLines(Path.of(tempDir.toString(), "CHANGELOG.md"));
		final List<String> expectedContent =
				Files.readAllLines(Path.of(this.getClass().getClassLoader().getResource("ExpectedChangelog.md").toURI()));

		assertEquals(expectedContent.size(), actualContent.size());
		for (int i = 0; i < expectedContent.size(); i++) {
			assertEquals(expectedContent.get(i), actualContent.get(i), "Lines "+i+" is are not equals");
		}
	}
}