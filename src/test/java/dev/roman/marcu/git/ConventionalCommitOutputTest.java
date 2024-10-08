package dev.roman.marcu.git;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import dev.roman.marcu.ConventionalCommit;
import dev.roman.marcu.output.ConventionalCommitModel;
import dev.roman.marcu.output.ConventionalCommitOutput;
import freemarker.template.TemplateException;

class ConventionalCommitOutputTest {

	@TempDir
	Path tempDir;

	@Test
	void testWrite() throws TemplateException, IOException, URISyntaxException {
		final Path outputFile = Path.of(tempDir.toString(), "CHANGELOG.md");
		final Path templateFile = Path.of("src", "main", "resources", "templates", "CHANGELOG.md.ftl");
		final ConventionalCommit commit = new ConventionalCommit("feat", false, "test", "Junit test", null, null);
		ConventionalCommitModel model = new ConventionalCommitModel("1.2.3", Collections.singletonList(commit));
		ConventionalCommitOutput.write(model, templateFile.toString(), outputFile.toString());

		assertEquals(true, Files.exists(outputFile));
		final List<String> actualContent = Files.readAllLines(outputFile);
		final List<String> expectedContent =
				Files.readAllLines(
						Path.of(this.getClass().getClassLoader().getResource("ExpectedChangelog.md").toURI()));

		assertEquals(expectedContent, actualContent);
	}

	@Test
	void testWriteWithAppendTrue() throws TemplateException, IOException, URISyntaxException {
		final Path outputFile = Path.of(tempDir.toString(), "CHANGELOG.md");
		final Path templateFile = Path.of("src", "main", "resources", "templates", "CHANGELOG.md.ftl");
		final ConventionalCommit commit1 = new ConventionalCommit("feat", false, "test", "Junit test 1", null, null);
		ConventionalCommitModel model = new ConventionalCommitModel("1.2.3", Collections.singletonList(commit1));
		ConventionalCommitOutput.write(model, templateFile.toString(), outputFile.toString());
		final ConventionalCommit commit2 = new ConventionalCommit("feat", false, "test", "Junit test 2", null, null);
		model = new ConventionalCommitModel("3.2.1", Collections.singletonList(commit2));
		ConventionalCommitOutput.write(model, templateFile.toString(), outputFile.toString());

		assertEquals(true, Files.exists(outputFile));
		final List<String> actualContent = Files.readAllLines(outputFile);
		final List<String> expectedContent =
				Files.readAllLines(
						Path.of(this.getClass().getClassLoader().getResource("ExpectedChangelogWithAppend.md")
										.toURI()));

		assertEquals(expectedContent, actualContent);
	}
}