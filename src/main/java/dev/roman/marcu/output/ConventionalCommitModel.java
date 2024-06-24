package dev.roman.marcu.output;

import java.util.List;

import dev.roman.marcu.ConventionalCommit;

public class ConventionalCommitModel {
	final String projectVersion;
	final List<ConventionalCommit> commits;

	public ConventionalCommitModel(final String projectVersion, final List<ConventionalCommit> commits) {
		this.projectVersion = projectVersion;
		this.commits = commits;
	}

	public String getProjectVersion() {
		return projectVersion;
	}

	public List<ConventionalCommit> getCommits() {
		return commits;
	}
}
