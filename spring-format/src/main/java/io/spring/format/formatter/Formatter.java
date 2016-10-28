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

import io.spring.formatter.eclipse.formatter.DefaultCodeFormatter;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

/**
 * TODO.
 *
 * @author Phillip Webb
 */
public class Formatter extends CodeFormatter {

	/**
	 * TODO.
	 */
	public static final int DEFAULT_KIND = CodeFormatter.K_COMPILATION_UNIT
			| CodeFormatter.F_INCLUDE_COMMENTS;

	/**
	 * TODO.
	 */
	public static final int DEFAULT_INDENTATION_LEVEL = 0;

	/**
	 * TODO.
	 */
	public static final String DEFAULT_LINE_SEPARATOR = null;

	private CodeFormatter delegate = new DelegateCodeFormatter();

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

	public TextEdit format(String source, int offset, int length) {
		return format(DEFAULT_KIND, source, offset, length, DEFAULT_INDENTATION_LEVEL,
				DEFAULT_LINE_SEPARATOR);
	}

	@Override
	public TextEdit format(int kind, String source, int offset, int length,
			int indentationLevel, String lineSeparator) {
		return this.delegate.format(kind, source, offset, length, indentationLevel,
				lineSeparator);
	}

	public TextEdit format(String source, IRegion[] regions) {
		return format(DEFAULT_KIND, source, regions, DEFAULT_INDENTATION_LEVEL,
				DEFAULT_LINE_SEPARATOR);
	}

	@Override
	public TextEdit format(int kind, String source, IRegion[] regions,
			int indentationLevel, String lineSeparator) {
		return this.delegate.format(kind, source, regions, indentationLevel,
				lineSeparator);
	}

	private static class DelegateCodeFormatter extends DefaultCodeFormatter {

		DelegateCodeFormatter() {
			super(loadOptions());
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
