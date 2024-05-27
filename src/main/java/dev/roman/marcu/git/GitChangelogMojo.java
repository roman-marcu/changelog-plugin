package dev.roman.marcu.git;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ReflogEntry;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import freemarker.template.TemplateException;

@Mojo(name = "git-changelog", defaultPhase = LifecyclePhase.DEPLOY, threadSafe = true)
public class GitChangelogMojo extends AbstractMojo {
	@Parameter(defaultValue = "${project.build.outputDirectory}")
	String outputDirectory;
	@Parameter(defaultValue = "CHANGELOG.md")
	String template;
	@Parameter(defaultValue = "${project.basedir}")
	File projectDirectory;
	@Parameter(readonly = true, required = true)
	String startRevision;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
			Repository db = builder.setGitDir(projectDirectory)
									.readEnvironment() // scan environment GIT_* variables
									.findGitDir() // scan up the file system tree
									.build();

			Git git = new Git(db);
			final Collection<ReflogEntry> refLogs = git.reflog().setRef(startRevision).call();
			final List<ConventionalCommit> commits = refLogs.stream()
															 .map(ConventionalCommitParser::parseCommit)
															 .collect(Collectors.toList());
			ConventionalCommitOutput.write(commits, template, outputDirectory);
		} catch (IOException | GitAPIException | TemplateException e) {
			throw new MojoExecutionException("Could not find load git repository");
		}
	}
}
