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

/**
 * {@link FormatterTask} to apply formatting.
 *
 * @author Phillip Webb
 */
public class ApplyTask extends FormatterTask {

	static final String NAME = "springFormatApply";

	static final String DESCRIPTION = "Formats Java source code using Spring conventions";

	@Override
	protected void apply(Iterable<File> files, Charset charset) {
	}

}
