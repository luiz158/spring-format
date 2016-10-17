package io.spring.format.formatter;

import java.io.IOException;

import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

import io.spring.format.eclipse.jdf.formatter.SpringCodeFormatter;

public class SpringFormatter {

	private SpringCodeFormatter codeFormatter;

	public SpringFormatter() {
		this.codeFormatter = new SpringCodeFormatter();
	}

	public String doFormat(String code) throws IOException, BadLocationException {
		TextEdit te = this.codeFormatter.format(CodeFormatter.K_COMPILATION_UNIT, code, 0, code.length(), 0, null);
		IDocument doc = new Document(code);
		te.apply(doc);
		String formattedCode = doc.get();
		return formattedCode;
	}

}
