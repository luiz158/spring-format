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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiBundle;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.util.IncorrectOperationException;
import io.spring.format.formatter.Formatter;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.text.edits.TextEdit;

/**
 * TODO.
 *
 * @author Phillip Webb
 */
public class SpringCodeStyleManager extends DelegatingCodeStyleManager {

	private static final Formatter FORMATTER = new Formatter();

	public SpringCodeStyleManager(CodeStyleManager delegate) {
		super(delegate);
	}

	@Override
	public void reformatText(PsiFile file, int startOffset, int endOffset)
			throws IncorrectOperationException {
		if (canFormat(file)) {
			TextRange range = new TextRange(startOffset, endOffset);
			format(file, Collections.singleton(range));
			return;
		}
		super.reformatText(file, startOffset, endOffset);
	}

	@Override
	public void reformatText(PsiFile file, Collection<TextRange> ranges)
			throws IncorrectOperationException {
		if (canFormat(file)) {
			format(file, ranges);
			return;
		}
		super.reformatTextWithContext(file, ranges);
	}

	@Override
	public void reformatTextWithContext(PsiFile file, Collection<TextRange> ranges)
			throws IncorrectOperationException {
		if (canFormat(file)) {
			format(file, ranges);
			return;
		}
		super.reformatTextWithContext(file, ranges);
	}

	private boolean canFormat(PsiFile file) {
		return StdFileTypes.JAVA.equals(file.getFileType());
	}

	private void format(PsiFile file, Collection<TextRange> ranges) {
		ApplicationManager.getApplication().assertWriteAccessAllowed();
		PsiDocumentManager documentManager = PsiDocumentManager.getInstance(getProject());
		documentManager.commitAllDocuments();
		checkWritable(file);
		Document document = documentManager.getDocument(file);
		if (document != null) {
			String source = document.getText();
			System.out.println("---------");
			System.out.println(source);
			System.out.println("---------");
			System.out.println(FORMATTER.format(source));
			System.out.println("---------");
			IRegion[] regions = asRegions(ranges);
			applyEdit(document, FORMATTER.format(source, regions));
		}
	}

	private void applyEdit(Document document, TextEdit textEdit) {
		System.out.println("running");
		WriteCommandAction.runWriteCommandAction(getProject(), () -> {
			try {
				EclipseDocumentAdapter adapter = new EclipseDocumentAdapter(document);
				System.out.println("apply");
				textEdit.apply(adapter);
				System.out.println("done");
				System.out.println(adapter.get());
				PsiDocumentManager.getInstance(getProject()).commitDocument(document);
			}
			catch (Exception ex) {
				throw new IllegalStateException(ex);
			}
		});
	}

	private IRegion[] asRegions(Collection<TextRange> ranges) {
		if (ranges == null) {
			return null;
		}
		List<IRegion> regions = new ArrayList<>(ranges.size());
		for (TextRange range : ranges) {
			regions.add(new Region(range.getStartOffset(), range.getLength()));
		}
		return regions.toArray(new IRegion[regions.size()]);
	}

	private void checkWritable(final PsiElement element)
			throws IncorrectOperationException {
		if (!element.isWritable()) {
			if (element instanceof PsiDirectory) {
				String url = ((PsiDirectory) element).getVirtualFile()
						.getPresentableUrl();
				throw new IncorrectOperationException(
						PsiBundle.message("cannot.modify.a.read.only.directory", url));
			}
			else {
				PsiFile file = element.getContainingFile();
				if (file == null) {
					throw new IncorrectOperationException();
				}
				VirtualFile virtualFile = file.getVirtualFile();
				if (virtualFile == null) {
					throw new IncorrectOperationException();
				}
				throw new IncorrectOperationException(
						PsiBundle.message("cannot.modify.a.read.only.file",
								virtualFile.getPresentableUrl()));
			}
		}
	}
}
