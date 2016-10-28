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

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import org.picocontainer.MutablePicoContainer;

/**
 * TODO.
 *
 * @author Phillip Webb
 */
public class SpringFormatComponent extends AbstractProjectComponent {

	private static final String CODE_STYLE_MANAGER_KEY = CodeStyleManager.class.getName();

	private static final Logger logger = Logger.getInstance(SpringFormatComponent.class);

	protected SpringFormatComponent(Project project) {
		super(project);
	}

	@Override
	public void initComponent() {
		VirtualFile baseDir = this.myProject.getBaseDir();
		VirtualFile springFormatFile = (baseDir == null ? null
				: baseDir.findChild(".springformat"));
		logger.debug("Initialized SpringFormatComponent for baseDir ", baseDir,
				" '(.springformatfile ", (springFormatFile == null ? "not " : ""),
				"found)");
		if (springFormatFile == null) {
			uninstall();
		}
		else {
			install();
		}
	}

	private void uninstall() {
		CodeStyleManager manager = CodeStyleManager.getInstance(this.myProject);
		if (manager != null && manager instanceof SpringCodeStyleManager) {
			logger.debug("Uninstalling SpringCodeStyleManager");
			install(((SpringCodeStyleManager) manager).getDelegate());
		}
	}

	private void install() {
		CodeStyleManager manager = CodeStyleManager.getInstance(this.myProject);
		if (manager != null && !(manager instanceof SpringCodeStyleManager)) {
			logger.debug("Installing SpringCodeStyleManager");
			install(new SpringCodeStyleManager(manager));
		}
	}

	private void install(CodeStyleManager manager) {
		MutablePicoContainer container = (MutablePicoContainer) this.myProject
				.getPicoContainer();
		container.unregisterComponent(CODE_STYLE_MANAGER_KEY);
		container.registerComponentInstance(CODE_STYLE_MANAGER_KEY, manager);
	}

}
