package io.spring.format.eclipse.jdf.formatter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import io.spring.format.formatter.SpringFormatter;

public class SpringFormatterTests {

	@Test
	public void test() throws Exception {
		SpringFormatter f = new SpringFormatter();
		InputStream stream = getClass().getResourceAsStream("simple-input.txt");
		String code = read(stream);
		String formatted = f.doFormat(code);
		System.out.println(formatted);
	}

	private String read(InputStream inputStream) throws Exception {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
		    result.write(buffer, 0, length);
		}
		return result.toString("UTF-8");
	}

}
