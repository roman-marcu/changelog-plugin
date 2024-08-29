package dev.roman.marcu.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.TimeZone;

import dev.roman.marcu.output.freemarker.ExternalTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class ConventionalCommitOutput {

	private static Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);

	static {
		cfg.setDefaultEncoding("UTF-8");
		cfg.setLocale(Locale.ENGLISH);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		cfg.setWrapUncheckedExceptions(true);
		cfg.setFallbackOnNullLoopVariable(false);
		cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault());
		cfg.setTemplateLoader(new ExternalTemplateLoader());
		cfg.setLocalizedLookup(false);
	}

	private ConventionalCommitOutput() {
	}

	public static void write(final ConventionalCommitModel model, final String templateName, final String output)
			throws IOException, TemplateException {
		final Template template = cfg.getTemplate(templateName);
		final File outputFile = Paths.get(output).toFile();
		final boolean append = outputFile.exists();
		try (Writer out = new OutputStreamWriter(new FileOutputStream(outputFile, append))) {
			template.process(model, out);
		}
	}
}
