package dev.roman.marcu.git;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.util.SystemReader;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.io.TempDir;

public class GitRepositoryTest {
	protected static Git git;
	protected static File repositoryDirectory;
	@TempDir
	static File sharedTempDir;

	protected static File initRepo() throws GitAPIException {
		repositoryDirectory = new File(sharedTempDir, "git-test-" + System.nanoTime());
		assertTrue(repositoryDirectory.mkdir());

		Git.init().setDirectory(repositoryDirectory).setBare(false).call();
		File repo = new File(repositoryDirectory, Constants.DOT_GIT);
		assertTrue(repo.exists());
		repo.deleteOnExit();
		return repo;
	}

	@BeforeAll
	public static void setUp() throws Exception {
		SystemReader.getInstance().getUserConfig().clear();
		git = Git.open(initRepo());
	}

	@AfterAll
	public static void tearDown() throws Exception {
		if (git != null) {
			git.close();
		}
	}

	protected static RevCommit buildCommit(String message) throws Exception {
		RevCommit commit = git.commit().setMessage(message).call();
		assertNotNull(commit);
		return commit;
	}

}
