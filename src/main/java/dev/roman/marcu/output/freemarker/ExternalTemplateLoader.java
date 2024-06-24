package dev.roman.marcu.output.freemarker;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import dev.roman.marcu.exceptions.TemplateUrlException;
import freemarker.cache.URLTemplateLoader;

public class ExternalTemplateLoader extends URLTemplateLoader {

	@Override
	protected URL getURL(final String templateUrl) {
		try {
			final Path path = Paths.get(templateUrl);
			if (path != null && path.toFile().exists()) {
				return path.toUri().toURL();
			} else {
				throw new TemplateUrlException("Path don't exist: " + templateUrl);
			}
		} catch (MalformedURLException e) {
			throw new TemplateUrlException(e);
		}
	}
}
