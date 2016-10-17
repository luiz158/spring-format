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
			String formatted = formatter.format(source);
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
