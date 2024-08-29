package dev.roman.marcu.git;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.StopWalkException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import dev.roman.marcu.ConventionalCommit;
import dev.roman.marcu.ConventionalCommitManager;
import dev.roman.marcu.exceptions.CommitReadingException;
import dev.roman.marcu.exceptions.CommitWritingException;
import dev.roman.marcu.output.ConventionalCommitModel;
import dev.roman.marcu.output.ConventionalCommitOutput;
import freemarker.template.TemplateException;

public class GitCommitManager implements ConventionalCommitManager<RevCommit, ConventionalCommit> {
	private final GitCommitParser gitCommitParser;

	public GitCommitManager(final GitCommitParser gitCommitParser) {
		this.gitCommitParser = gitCommitParser;
	}

	@Override
	public Iterable<RevCommit> getCommits(final File commitsRepositoryDirectory, final String startRevision,
			final String endRevision) {
		final Iterable<RevCommit> results;
		try {
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
			Repository db = builder.setGitDir(
							new File(commitsRepositoryDirectory.getPath(), Constants.DOT_GIT))
									.readEnvironment() // scan environment GIT_* variables
									.findGitDir() // scan up the file system tree
									.setMustExist(true)
									.build();

			Git git = new Git(db);
			LogCommand gitCommand;
			if (startRevision == null || startRevision.isBlank()) {
				gitCommand = git.log().all();
			} else {
				final ObjectId start = git.getRepository().resolve(startRevision);
				final ObjectId end = git.getRepository().resolve(endRevision);
				if (start == null || end == null)
					throw new CommitReadingException(
							"Could not find logs from: " + startRevision + " to:" + endRevision);
				gitCommand = git.log().addRange(start, end);
			}
			results = gitCommand.setRevFilter(buildRevFilter()).call();

		} catch (IOException | GitAPIException e) {
			throw new CommitReadingException(e);
		}
		return results;
	}

	private RevFilter buildRevFilter() {
		return new RevFilter() {
			@Override
			public boolean include(final RevWalk revWalk, final RevCommit revCommit) throws StopWalkException {
				return revCommit.getType() == Constants.OBJ_COMMIT;
			}

			@Override
			public RevFilter clone() {
				return this;
			}
		};
	}

	@Override
	public Optional<ConventionalCommit> convert(final RevCommit commit) {
		return gitCommitParser.buildCommit(commit);
	}

	@Override
	public void writeTo(final String template, final String outputFile,
			final String projectVersion, final List<ConventionalCommit> commits) {
		try {
			ConventionalCommitModel model = new ConventionalCommitModel(projectVersion, commits);
			ConventionalCommitOutput.write(model, template, outputFile);
		} catch (IOException | TemplateException e) {
			throw new CommitWritingException(e);
		}
	}
}
