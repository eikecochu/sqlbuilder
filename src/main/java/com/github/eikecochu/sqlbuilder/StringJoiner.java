package com.github.eikecochu.sqlbuilder;

import java.util.ArrayList;
import java.util.List;

public class StringJoiner {

	private final List<String> strings = new ArrayList<>();

	public StringJoiner add(final String string) {
		strings.add(string);
		return this;
	}

	@Override
	public String toString() {
		return toString(null);
	}

	public String toString(final String delimiter) {
		if (strings.isEmpty())
			return null;
		final String s = StringUtils.join(strings, delimiter);
		if (s.isBlank())
			return null;
		return s;
	}

	public boolean isEmpty() {
		return strings.isEmpty();
	}

}
