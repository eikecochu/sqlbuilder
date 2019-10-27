package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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
		return StringUtils.join(strings, delimiter);
	}

	public boolean isEmpty() {
		return strings.isEmpty();
	}

}
