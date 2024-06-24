package dev.roman.marcu.git;

import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.jgit.revwalk.RevCommit;

import dev.roman.marcu.CommonChangelogMojo;
import dev.roman.marcu.ConventionalCommit;

@Mojo(name = "git-changelog", threadSafe = true)
public class GitChangelogMojo extends CommonChangelogMojo<RevCommit, ConventionalCommit> {
	public GitChangelogMojo() {
		super(new GitCommitManager(new GitCommitParser()));
	}

}
