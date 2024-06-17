package dev.roman.marcu.git;

import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GitCommitManagerTest extends GitRepositoryTest {

	@Test
	void getCommits() throws Exception {
		buildCommit("test1");
		GitCommitManager commitManager = new GitCommitManager(new GitCommitParser());
		final Iterable<RevCommit> commits = commitManager.getCommits(repositoryDirectory, null,
				null);
		Assertions.assertEquals(true, commits.iterator().hasNext());
	}
}