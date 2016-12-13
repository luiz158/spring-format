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

package io.spring.format.maven;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;

/**
 * Base class for formatter Mojo.
 *
 * @author Phillip Webb
 */
public abstract class FormatMojo extends AbstractMojo {

	private static final String[] DEFAULT_INCLUDES = new String[] { "**/*.java" };
	/**
	 * The Maven Project Object.
	 */
	@Parameter(defaultValue = "${project}", readonly = true, required = true)
	protected MavenProject project;

	/**
	 * Specifies the location of the source directories to use.
	 */
	@Parameter(defaultValue = "${project.compileSourceRoots}")
	private List<String> sourceDirectories;

	/**
	 * Specifies the location of the test source directories to use.
	 */
	@Parameter(defaultValue = "${project.testCompileSourceRoots}")
	private List<String> testSourceDirectories;

	/**
	 * Specifies the names filter of the source files to be excluded.
	 */
	@Parameter(property = "spring-format.excludes")
	private String[] excludes;

	/**
	 * Specifies the names filter of the source files to be included.
	 */
	@Parameter(property = "spring-format.includes")
	private String[] includes;

	/**
	 * The file encoding to use when reading the source files. If the property
	 * <code>project.build.sourceEncoding</code> is not set, the platform default encoding
	 * is used..
	 */
	@Parameter(property = "encoding", defaultValue = "${project.build.sourceEncoding}")
	private String encoding;

	private List<File> resolveFiles(List<String> directories) {
		List<File> resolved = new ArrayList<>(directories.size());
		for (String dir : directories) {
			resolved.add(FileUtils.resolveFile(this.project.getBasedir(), dir));
		}
		return resolved;
	}

	Iterable<File> addCollectionFiles(File newBasedir) throws IOException {
		DirectoryScanner ds = new DirectoryScanner();
		ds.setBasedir(newBasedir);
		if (this.includes != null && this.includes.length > 0) {
			ds.setIncludes(this.includes);
		}
		else {
			ds.setIncludes(DEFAULT_INCLUDES);
		}
		ds.setExcludes(this.excludes);
		ds.addDefaultExcludes();
		ds.setCaseSensitive(false);
		ds.setFollowSymlinks(false);
		ds.scan();
		List<File> foundFiles = new ArrayList<>();
		for (String filename : ds.getIncludedFiles()) {
			foundFiles.add(new File(newBasedir, filename));
		}
		return foundFiles;
	}

}
