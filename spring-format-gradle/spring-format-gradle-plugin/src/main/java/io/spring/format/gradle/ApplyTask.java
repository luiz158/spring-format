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

package io.spring.format.gradle;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import io.spring.format.formatter.Formatter;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.gradle.api.GradleException;

/**
 * {@link FormatterTask} to apply formatting.
 *
 * @author Phillip Webb
 */
public class ApplyTask extends FormatterTask {

	static final String NAME = "springFormatApply";

	static final String DESCRIPTION = "Formats Java source code using Spring conventions";

	@Override
	protected void apply(Iterable<File> files, Charset encoding) throws Exception {
		for (File file : files) {
			byte[] bytes = Files.readAllBytes(file.toPath());
			String content = new String(bytes, encoding);
			try {
				TextEdit edit = new Formatter(false).format(content);
				if (edit.hasChildren() || edit.getLength() > 0) {
					IDocument document = new Document(content);
					edit.apply(document);
					Files.write(file.toPath(), document.get().getBytes(encoding),
							StandardOpenOption.TRUNCATE_EXISTING);
				}
			}
			catch (Exception ex) {
				throw new GradleException("Unable to format file " + file, ex);
			}
		}
	}

}
