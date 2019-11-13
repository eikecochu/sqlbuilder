package com.github.eikecochu.sqlbuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * The StringJoiner class is used to assemble the query string with support for
 * adding strings and collections of strings and optionally joining them with a
 * delimiter.
 */
public final class StringJoiner {

	private final List<String> strings = new ArrayList<>();

	/**
	 * Add a string
	 *
	 * @param string The string to add
	 * @return This instance
	 */
	public StringJoiner add(final String string) {
		strings.add(string);
		return this;
	}

	@Override
	public String toString() {
		return toString(null);
	}

	/**
	 * Assembles all strings, separated by a delimiter
	 *
	 * @param delimiter The delimiter
	 * @return The assembled string
	 */
	public String toString(final String delimiter) {
		if (strings.isEmpty())
			return null;
		final String s = StringUtils.join(strings, delimiter);
		if (s.trim()
				.isEmpty())
			return null;
		return s.replaceAll("(\r?\n)+$", "");
	}

	/**
	 * Checks if this StringJoiner contains strings
	 *
	 * @return true if it contains strings
	 */
	public boolean notEmpty() {
		return !strings.isEmpty();
	}

}
