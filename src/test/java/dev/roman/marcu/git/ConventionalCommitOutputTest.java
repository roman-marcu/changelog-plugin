package dev.roman.marcu.git;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import edu.emory.mathcs.backport.java.util.Collections;
import freemarker.template.TemplateException;

class ConventionalCommitOutputTest {

	@TempDir
	Path tempDir;

	@Test
	void testWrite() throws TemplateException, IOException {
		final ConventionalCommit commit = new ConventionalCommit(null, false, "", "", "", null);
		ConventionalCommitOutput.write(Collections.singletonList(commit), "RELEASE.md", tempDir.toString());

		Assertions.assertEquals(true, Files.exists(Path.of(tempDir.toString(), "RELEASE.md")));
	}
}