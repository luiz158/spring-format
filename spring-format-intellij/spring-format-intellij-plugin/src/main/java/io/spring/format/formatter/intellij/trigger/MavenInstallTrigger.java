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

import com.intellij.openapi.project.DumbAwareRunnable;
import com.intellij.openapi.project.Project;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.jetbrains.idea.maven.utils.MavenUtil;

/**
 * {@link InstallTrigger} for Maven builds.
 *
 * @author Phillip Webb
 */
public class MavenInstallTrigger implements InstallTrigger {

	@Override
	public boolean isSpringFormatted(Project project) {

		MavenUtil.runWhenInitialized(project, new DumbAwareRunnable() {

			@Override
			public void run() {
				MavenProjectsManager manager = MavenProjectsManager.getInstance(project);
				for (MavenProject mavenProject : manager.getRootProjects()) {
					System.out.println(mavenProject);
				}
			}

		});
		MavenProjectsManager manager = MavenProjectsManager.getInstance(project);
		if (manager == null) {
			return false;
		}
		for (MavenProject mavenProject : manager.getRootProjects()) {
			if (isSpringFormatted(mavenProject)) {
				return true;
			}
		}
		return false;
	}

	private boolean isSpringFormatted(MavenProject mavenProject) {
		System.out.println(mavenProject);
		return false;
	}

}
