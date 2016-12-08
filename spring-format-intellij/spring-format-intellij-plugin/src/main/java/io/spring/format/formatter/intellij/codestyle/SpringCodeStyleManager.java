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

package io.spring.format.formatter.intellij.codestyle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

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
 * {@link CodeStyleManager} to apply Spring Formatting conventions.
 *
 * @author Phillip Webb
 */
public class SpringCodeStyleManager extends DelegatingCodeStyleManager {

	private static final IRegion[] NO_REGIONS = {};

	public SpringCodeStyleManager(CodeStyleManager delegate) {
		super(delegate);
	}

	@Override
	public void reformatText(PsiFile file, int startOffset, int endOffset)
			throws IncorrectOperationException {
		reformat(file, () -> Collections.singleton(new TextRange(startOffset, endOffset)),
				() -> super.reformatText(file, startOffset, endOffset));
	}

	@Override
	public void reformatText(PsiFile file, Collection<TextRange> ranges)
			throws IncorrectOperationException {
		reformat(file, () -> ranges, () -> super.reformatText(file, ranges));
	}

	@Override
	public void reformatTextWithContext(PsiFile file, Collection<TextRange> ranges)
			throws IncorrectOperationException {
		reformat(file, () -> ranges, () -> super.reformatTextWithContext(file, ranges));
	}

	private void reformat(PsiFile file, Supplier<Collection<TextRange>> ranges,
			Runnable delegate) {
		if (StdFileTypes.JAVA.equals(file.getFileType())) {
			applySpringFormatting(file, ranges.get());
		}
		else {
			delegate.run();
		}
	}

	private void applySpringFormatting(PsiFile file, Collection<TextRange> ranges) {
		ApplicationManager.getApplication().assertWriteAccessAllowed();
		PsiDocumentManager documentManager = PsiDocumentManager.getInstance(getProject());
		documentManager.commitAllDocuments();
		if (!file.isWritable()) {
			throwNotWritableException(file);
		}
		applySpringFormatting(file, ranges, documentManager.getDocument(file));
	}

	private void applySpringFormatting(PsiFile file, Collection<TextRange> ranges,
			Document document) {
		if (document != null) {
			Formatter formatter = new Formatter();
			String source = document.getText();
			IRegion[] regions = asRegions(ranges);
			applyEdit(document, formatter.format(source, regions));
		}
	}

	private void throwNotWritableException(PsiElement element)
			throws IncorrectOperationException {
		if (element instanceof PsiDirectory) {
			String url = ((PsiDirectory) element).getVirtualFile().getPresentableUrl();
			throw new IncorrectOperationException(
					PsiBundle.message("cannot.modify.a.read.only.directory", url));
		}
		PsiFile file = element.getContainingFile();
		if (file == null) {
			throw new IncorrectOperationException();
		}
		VirtualFile virtualFile = file.getVirtualFile();
		if (virtualFile == null) {
			throw new IncorrectOperationException();
		}
		throw new IncorrectOperationException(PsiBundle.message(
				"cannot.modify.a.read.only.file", virtualFile.getPresentableUrl()));
	}

	private IRegion[] asRegions(Collection<TextRange> ranges) {
		if (ranges == null) {
			return NO_REGIONS;
		}
		List<IRegion> regions = new ArrayList<>(ranges.size());
		for (TextRange range : ranges) {
			regions.add(new Region(range.getStartOffset(), range.getLength()));
		}
		return regions.toArray(new IRegion[regions.size()]);
	}

	private void applyEdit(Document document, TextEdit textEdit) {
		WriteCommandAction.runWriteCommandAction(getProject(), () -> {
			try {
				EclipseDocumentAdapter adapter = new EclipseDocumentAdapter(document);
				textEdit.apply(adapter);
				PsiDocumentManager.getInstance(getProject()).commitDocument(document);
			}
			catch (Exception ex) {
				throw new IllegalStateException(ex);
			}
		});
	}

}
