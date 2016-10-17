package io.spring.format.eclipse.jdf.formatter;

import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.IRegion;
import org.eclipse.text.edits.TextEdit;

public class SpringFormatter extends CodeFormatter {

	@Override
	public TextEdit format(int kind, String source, int offset, int length, int indentationLevel, String lineSeparator) {
		return null;
	}

	@Override
	public TextEdit format(int kind, String source, IRegion[] regions, int indentationLevel, String lineSeparator) {
		return null;
	}

	@Override
	public String createIndentationString(int indentationLevel) {
		return null;
	}

}
