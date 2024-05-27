package dev.roman.marcu.git;

import java.util.HashMap;
import java.util.Map;

public record ConventionalCommit(String type, boolean isBreakingChange, String scope, String description, String body,
		Map<String, String> footers) {

	public static final class Builder {
		String type;
		boolean isBreakingChange;
		String scope;
		String description;
		StringBuilder body;
		Map<String, String> footers;

		public Builder(final String type, String description) {
			this.type = type;
			this.description = description;
			this.isBreakingChange = false;
			this.footers = new HashMap<>();
			this.body = new StringBuilder();
		}

		public Builder setBreakingChange(final boolean breakingChange) {
			isBreakingChange = breakingChange;
			return this;
		}

		public Builder setScope(final String scope) {
			this.scope = scope;
			return this;
		}

		public Builder setBody(final String body) {
			this.body.append(body);
			return this;
		}

		public Builder addFooter(final String key, final String footer) {
			this.footers.put(key, footer);
			return this;
		}

		public ConventionalCommit build() {
			return new ConventionalCommit(type, isBreakingChange, scope, description, body.toString(), footers);
		}
	}
}
