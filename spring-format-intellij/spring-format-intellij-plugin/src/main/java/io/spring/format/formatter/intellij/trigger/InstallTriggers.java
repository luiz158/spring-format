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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import io.spring.format.formatter.intellij.SpringFormatComponent;

/**
 * The collection of {@link InstallTrigger} instances called to check if Spring formatting
 * should be applied.
 *
 * @author Phillip Webb
 */
public class InstallTriggers {

	private static final List<InstallTrigger> TRIGGERS;

	static {
		List<InstallTrigger> triggers = new ArrayList<>();
		triggers.add(new FileInstallTrigger());
		triggers.add(new MavenInstallTrigger());
		triggers.add(new GradleInstallTrigger());
		TRIGGERS = Collections.unmodifiableList(triggers);
	}

	private static final Logger logger = Logger.getInstance(SpringFormatComponent.class);

	/**
	 * Determine if the given project should be formatted using Spring's conventions.
	 * @param project the source project
	 * @return {@code true} if the project should be formatted using Spring's convetions.
	 */
	public boolean isSpringFormatted(Project project) {
		for (InstallTrigger trigger : TRIGGERS) {
			try {
				if (trigger.isSpringFormatted(project)) {
					logger.debug("Trigger " + trigger.getClass()
							+ " indicated formatting is required");
					return true;
				}
				logger.debug("Trigger " + trigger.getClass()
						+ " indicated formatting is not required");
			}
			catch (Throwable ex) {
				logger.debug("Trigger " + trigger.getClass() + " resulted in error", ex);
			}
		}
		return false;
	}

}
