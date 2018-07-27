package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

class StringJoiner {

	private List<String> strings = new ArrayList<>();

	public StringJoiner add(String string) {
		strings.add(string);
		return this;
	}

	@Override
	public String toString() {
		return toString(null);
	}

	public String toString(String delimiter) {
		return StringUtils.join(strings, delimiter);
	}

	public boolean isEmpty() {
		return strings.isEmpty();
	}

}
