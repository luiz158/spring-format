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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileAdapter;
import com.intellij.openapi.vfs.VirtualFileCopyEvent;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileMoveEvent;
import com.intellij.openapi.vfs.VirtualFilePropertyEvent;

/**
 * {@link Monitor} that looks for a {@literal .springformat} file.
 *
 * @author Phillip Webb
 */
public class FileMonitor extends Monitor {

	private static final String TRIGGER_FILE = ".springformat";

	private final VirtualFileManager fileManager;

	private final VirtualFileListener listener;

	public FileMonitor(Project project, Trigger trigger, VirtualFileManager fileManager) {
		super(project, trigger);
		this.fileManager = fileManager;
		this.listener = new Listener();
		fileManager.addVirtualFileListener(this.listener);
		VirtualFile baseDir = project.getBaseDir();
		VirtualFile triggerFile = (baseDir == null ? null
				: baseDir.findChild(".springformat"));
		trigger.updateState(
				triggerFile == null ? Trigger.State.NOT_ACTIVE : Trigger.State.ACTIVE);
	}

	@Override
	public void stop() {
		this.fileManager.removeVirtualFileListener(this.listener);
	}

	private void fileAdded(VirtualFile file) {
		if (isTriggerFile(file) && file.isValid()) {
			getTrigger().updateState(Trigger.State.ACTIVE);
		}
	}

	private void fileRemoved(VirtualFile file) {
		if (isTriggerFile(file)) {
			getTrigger().updateState(Trigger.State.NOT_ACTIVE);
		}
	}

	private boolean isTriggerFile(VirtualFile file) {
		VirtualFile parent = (file == null ? null : file.getParent());
		return (parent != null && parent.equals(getProject().getBaseDir())
				&& TRIGGER_FILE.equals(file.getName()));
	}

	public static Factory factory() {
		return (project, trigger) -> new FileMonitor(project, trigger,
				VirtualFileManager.getInstance());
	}

	private class Listener extends VirtualFileAdapter {

		@Override
		public void fileCreated(VirtualFileEvent event) {
			fileAdded(event.getFile());
		}

		@Override
		public void fileCopied(VirtualFileCopyEvent event) {
			fileAdded(event.getFile());
		}

		@Override
		public void fileDeleted(VirtualFileEvent event) {
			fileRemoved(event.getFile());
		}

		@Override
		public void fileMoved(VirtualFileMoveEvent event) {
			fileRemoved(event.getFile());
		}

		@Override
		public void propertyChanged(VirtualFilePropertyEvent event) {
			// FIXME
		}

	}

}
