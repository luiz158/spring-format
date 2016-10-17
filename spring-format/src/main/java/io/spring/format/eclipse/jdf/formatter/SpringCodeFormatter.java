package io.spring.format.eclipse.jdf.formatter;

import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.internal.formatter.DefaultCodeFormatter;
import org.eclipse.jface.text.IRegion;
import org.eclipse.text.edits.TextEdit;

public class SpringCodeFormatter extends CodeFormatter {

	// FIXME move to shade version

	private final CodeFormatter delegate;

	public SpringCodeFormatter() {
		delegate = new DefaultCodeFormatter();
	}

	@Override
	public TextEdit format(int kind, String source, int offset, int length, int indentationLevel, String lineSeparator) {
		return delegate.format(kind, source, offset, length, indentationLevel, lineSeparator);
	}

	@Override
	public TextEdit format(int kind, String source, IRegion[] regions, int indentationLevel, String lineSeparator) {
		return delegate.format(kind, source, regions, indentationLevel, lineSeparator);
	}

	@Override
	public String createIndentationString(int indentationLevel) {
		return delegate.createIndentationString(indentationLevel);
	}

}
