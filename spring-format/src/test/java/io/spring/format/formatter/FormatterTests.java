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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link Formatter}.
 */
@RunWith(Parameterized.class)
public class FormatterTests {

	private final File source;

	private final File expected;

	private final Formatter formatter;

	public FormatterTests(File source, File expected) {
		this.source = source;
		this.expected = expected;
		this.formatter = new Formatter();
	}

	@Test
	public void format() throws Exception {
		String sourceContent = read(this.source);
		String expectedContent = read(this.expected);
		String formattedContent = this.formatter.format(sourceContent);
		if (!expectedContent.equals(formattedContent)) {
			System.out.println(
					"Formatted " + this.source + " does not match " + this.expected);
			print("Source " + this.source, sourceContent);
			print("Expected +" + this.expected, expectedContent);
			print("Got", formattedContent);
			System.out.println("========================================");
			assertEquals("Formatted content does not match for " + this.source,
					expectedContent, formattedContent);
		}
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
		InputStream inputStream = new FileInputStream(file);
		try {
			return read(inputStream);
		}
		finally {
			inputStream.close();
		}
	}

	private String read(InputStream inputStream)
			throws IOException, UnsupportedEncodingException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}
		return result.toString("UTF-8");
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
