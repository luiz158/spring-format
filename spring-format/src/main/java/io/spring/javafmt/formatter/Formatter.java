package io.spring.javafmt.formatter;

import java.util.Collection;

public interface Formatter {

	default String format(String source) {
		return null;
	}

	default String format(String source, Collection<Range> characterRanges) {
		return null;
	}

	Replacements getFormatReplacements(String source, Collection<Range> characterRanges);

}
