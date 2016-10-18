import java.util.Collection;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.Indent;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.ThrowableRunnable;

public class Dunno extends CodeStyleManager {

	@Override
	public void adjustLineIndent(PsiFile arg0, TextRange arg1) throws IncorrectOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public int adjustLineIndent(PsiFile arg0, int arg1) throws IncorrectOperationException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int adjustLineIndent(Document arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String fillIndent(Indent arg0, FileType arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Indent getIndent(String arg0, FileType arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLineIndent(PsiFile arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLineIndent(Document arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Project getProject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLineToBeIndented(PsiFile arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSequentialProcessingAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void performActionWithFormatterDisabled(Runnable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends Throwable> void performActionWithFormatterDisabled(ThrowableRunnable<T> arg0) throws T {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> T performActionWithFormatterDisabled(Computable<T> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PsiElement reformat(PsiElement arg0) throws IncorrectOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PsiElement reformat(PsiElement arg0, boolean arg1) throws IncorrectOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reformatNewlyAddedElement(ASTNode arg0, ASTNode arg1) throws IncorrectOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public PsiElement reformatRange(PsiElement arg0, int arg1, int arg2) throws IncorrectOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PsiElement reformatRange(PsiElement arg0, int arg1, int arg2, boolean arg3)
			throws IncorrectOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reformatText(PsiFile arg0, Collection<TextRange> arg1) throws IncorrectOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void reformatText(PsiFile arg0, int arg1, int arg2) throws IncorrectOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void reformatTextWithContext(PsiFile arg0, Collection<TextRange> arg1) throws IncorrectOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public Indent zeroIndent() {
		// TODO Auto-generated method stub
		return null;
	}

}
