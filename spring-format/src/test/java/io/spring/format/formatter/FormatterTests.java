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

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link Formatter}.
 */
@RunWith(Parameterized.class)
public class FormatterTests {

	private static final Charset UTF8 = Charset.forName("UTF-8");

	private final File source;

	private final File expected;

	public FormatterTests(File source, File expected) {
		this.source = source;
		this.expected = expected;
	}

	@Test
	public void format() throws Exception {
		String sourceContent = read(this.source);
		String expectedContent = read(this.expected);
		String formattedContent = format(sourceContent);
		if (!expectedContent.equals(formattedContent)) {
			System.out.println(
					"Formatted " + this.source + " does not match " + this.expected);
			print("Source " + this.source, sourceContent);
			print("Expected +" + this.expected, expectedContent);
			print("Got", formattedContent);
			System.out.println("========================================");
			assertThat(expectedContent).isEqualTo(formattedContent)
					.describedAs("Formatted content does not match for " + this.source);
		}
	}

	private String format(String sourceContent) throws Exception {
		IDocument document = new Document(sourceContent);
		TextEdit textEdit = new Formatter(false).format(sourceContent);
		textEdit.apply(document);
		return document.get();
	}

	private void print(String name, String content) {
		System.out.println(name + ":");
		System.out.println();
		System.out.println("----------------------------------------");
		System.out.println(content);
		System.out.println("----------------------------------------");
		System.out.println();
		System.out.println();
	}

	private String read(File file) throws Exception {
		return new String(Files.readAllBytes(file.toPath()), UTF8);
	}

	@Parameters(name = "{0}")
	public static Collection<Object[]> files() {
		Collection<Object[]> files = new ArrayList<>();
		File sourceDir = new File("src/test/resources/source");
		File expectedDir = new File("src/test/resources/expected");
		for (File source : sourceDir.listFiles((dir, name) -> !name.startsWith("."))) {
			File expected = new File(expectedDir, source.getName());
			files.add(new Object[] { source, expected });
		}
		return files;
	}

}
