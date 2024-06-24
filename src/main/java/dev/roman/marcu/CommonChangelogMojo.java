package dev.roman.marcu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

public class CommonChangelogMojo<T, D> extends AbstractMojo {
	final ConventionalCommitManager commitManager;
	@Parameter(property = "template", required = true)
	String templateFilePath;
	@Parameter(property = "output", defaultValue = "${project.build.outputDirectory}/CHANGELOG.md")
	String outputFilePath;
	@Parameter(defaultValue = "${project.basedir}")
	File projectDirectory;
	@Parameter(property = "revision.start", readonly = true)
	String startRevision;
	@Parameter(property = "revision.end", readonly = true, defaultValue = "HEAD")
	String endRevision;

	@Parameter(property = "project.version", readonly = true, defaultValue = "${project.version}")
	String projectVersion;

	public CommonChangelogMojo(final ConventionalCommitManager commitManager) {
		this.commitManager = commitManager;
	}

	@Override
	public void execute() {
		final List<D> commits = new ArrayList<>();
		final Iterable<T> commitsIterator = commitManager.getCommits(projectDirectory, startRevision, endRevision);
		commitsIterator.forEach(commit -> commitManager.convert(commit).ifPresent(c -> commits.add((D) c)));
		getLog().debug("<<< Processed " + commits.size() + " commits");
		commitManager.writeTo(templateFilePath, outputFilePath, projectVersion, commits);
	}
}
