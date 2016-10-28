/*
 * Copyright 2012-2016 the original author or authors.
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

package io.spring.format.formatter.intellij;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import io.spring.format.formatter.Formatter;

/**
 * TODO.
 *
 * @author Phillip Webb
 */
public final class Test {

	private Test() {
	}

	public static void main(String[] args) {
		try {
			File file = new File(
					"/Users/pwebb/projects/spring-boot/code/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/SpringBootApplication.java");
			InputStream inputStream = new FileInputStream(file);
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}
			String source = result.toString("UTF-8");
			String format = new Formatter().format(source);
			System.out.println(source);
			System.out.println("---");
			System.out.println(format);

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
