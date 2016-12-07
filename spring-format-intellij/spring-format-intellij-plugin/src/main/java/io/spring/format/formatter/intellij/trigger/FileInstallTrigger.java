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

package io.spring.format.formatter.intellij.trigger;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import io.spring.format.formatter.intellij.SpringFormatComponent;

/**
 * {@link InstallTrigger} that looks for a {@literal .springformat} file in the project
 * root.
 *
 * @author Phillip Webb
 */
public class FileInstallTrigger implements InstallTrigger {

	private static final Logger logger = Logger.getInstance(SpringFormatComponent.class);

	@Override
	public boolean isSpringFormatted(Project project) {
		VirtualFile baseDir = project.getBaseDir();
		VirtualFile springFormatFile = (baseDir == null ? null
				: baseDir.findChild(".springformat"));
		logger.debug("Initialized SpringFormatComponent for baseDir ", baseDir,
				" '(.springformatfile ", (springFormatFile == null ? "not " : ""),
				"found)");
		return springFormatFile != null;
	}

}
