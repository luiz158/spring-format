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
import java.io.InputStream;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link Formatter}.
 */
public class FormatterTests {

	private static final String[] FILES = { "simple" };

	private Formatter formatter = new Formatter();

	@Test
	public void formatSourceFiles() throws Exception {
		for (String file : FILES) {
			String source = read(file + "-input.txt");
			String expected = read(file + "-expected.txt");
			String formatted = this.formatter.format(source);
			System.out.println("---");
			System.out.println(source);
			System.out.println("--->");
			System.out.println(formatted);
			System.out.println("---");
			assertThat(formatted).as(file).isEqualTo(expected);
		}
	}

	private String read(String name) throws Exception {
		InputStream inputStream = getClass().getResourceAsStream(name);
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}
		return result.toString("UTF-8");
	}

}
