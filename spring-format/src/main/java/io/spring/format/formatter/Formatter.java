/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.format.formatter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import io.spring.formatter.eclipse.formatter.ExtendedCodeFormatter;
import io.spring.formatter.eclipse.formatter.Preparator;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

/**
 * A {@link CodeFormatter} that applies Spring formatting conventions.
 *
 * @author Phillip Webb
 */
public class Formatter extends CodeFormatter {

	/**
	 * The components that will be formatted by default.
	 */
	private static final int DEFAULT_COMPONENTS = CodeFormatter.K_COMPILATION_UNIT
			| CodeFormatter.F_INCLUDE_COMMENTS;

	/**
	 * The default indentation level.
	 */
	private static final int DEFAULT_INDENTATION_LEVEL = 0;

	/**
	 * The default line separator.
	 */
	private static final String DEFAULT_LINE_SEPARATOR = null;

	private CodeFormatter delegate = new DelegateCodeFormatter();

	/**
	 * Format the given source content.
	 * @param source the source content to format
	 * @return the formatted content
	 */
	public String format(String source) {
		IDocument document = new Document(source);
		TextEdit textEdit = format(source, 0, source.length());
		try {
			textEdit.apply(document);
		}
		catch (MalformedTreeException | BadLocationException ex) {
			throw new IllegalStateException(ex);
		}
		return document.get();
	}

	/**
	 * Format a specific subsection of the given source content
	 * @param source the source content to format
	 * @param offset the offset to start formatting
	 * @param length the length to format
	 * @return the formatted content
	 */
	public TextEdit format(String source, int offset, int length) {
		String nlsWarnings = System.getProperty("osgi.nls.warnings");
		try {
			System.setProperty("osgi.nls.warnings", "ignore");
			return format(DEFAULT_COMPONENTS, source, offset, length,
					DEFAULT_INDENTATION_LEVEL, DEFAULT_LINE_SEPARATOR);
		}
		finally {
			if (nlsWarnings != null) {
				System.setProperty("osgi.nls.warnings", nlsWarnings);
			}
		}
	}

	@Override
	public TextEdit format(int kind, String source, int offset, int length,
			int indentationLevel, String lineSeparator) {
		return this.delegate.format(kind, source, offset, length, indentationLevel,
				lineSeparator);
	}

	/**
	 * Format specific subsections of the given source content
	 * @param source the source content to format
	 * @param regions the regions to format
	 * @return the formatted content
	 */
	public TextEdit format(String source, IRegion[] regions) {
		return format(DEFAULT_COMPONENTS, source, regions, DEFAULT_INDENTATION_LEVEL,
				DEFAULT_LINE_SEPARATOR);
	}

	@Override
	public TextEdit format(int kind, String source, IRegion[] regions,
			int indentationLevel, String lineSeparator) {
		return this.delegate.format(kind, source, regions, indentationLevel,
				lineSeparator);
	}

	/**
	 * Internal delegate code formatter to apply Spring {@literal formatter.prefs} and add
	 * {@link Preparator Preparators}.
	 */
	private static class DelegateCodeFormatter extends ExtendedCodeFormatter {

		DelegateCodeFormatter() {
			super(loadOptions());
			addPreparator(new JavadocWhitespacePreparator());
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private static Map<String, String> loadOptions() {
			try {
				Properties properties = new Properties();
				InputStream inputStream = Formatter.class
						.getResourceAsStream("formatter.prefs");
				try {
					properties.load(inputStream);
					return (Map) properties;
				}
				finally {
					inputStream.close();
				}
			}
			catch (IOException ex) {
				throw new IllegalStateException(ex);
			}
		}

	}

}
