package dev.roman.marcu;

import java.util.HashMap;
import java.util.Map;

public class ConventionalCommit {

	private final String type;
	private final boolean isBreakingChange;
	private final String scope;
	private final String description;
	private final String body;
	private final Map<String, String> footers;

	public ConventionalCommit(final String type, final boolean isBreakingChange, final String scope,
			final String description, final String body, final Map<String, String> footers) {
		this.type = type;
		this.isBreakingChange = isBreakingChange;
		this.scope = scope;
		this.description = description;
		this.body = body;
		this.footers = footers;
	}

	public String getType() {
		return type;
	}

	public boolean isBreakingChange() {
		return isBreakingChange;
	}

	public String getScope() {
		return scope;
	}

	public String getDescription() {
		return description;
	}

	public String getBody() {
		return body;
	}

	public Map<String, String> getFooters() {
		return footers;
	}

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
