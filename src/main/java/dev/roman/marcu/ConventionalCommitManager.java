package dev.roman.marcu;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface ConventionalCommitManager<T, D> {
	Iterable<T> getCommits(File commitsRepositoryDirectory, String startRevision, String endRevision);

	Optional<D> convert(T commit);

	void writeTo(String template, String outputDirectory, String version, List<D> commits);
}
