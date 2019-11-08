package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter(AccessLevel.PROTECTED)
public enum ConjunctiveOperator implements QueryPart {
	AND("AND"),
	OR("OR");

	private final String string;

	ConjunctiveOperator(final String string) {
		this.string = string;
	}

	@Override
	public String string(final QueryOptions options) {
		return options.cased(string);
	}
}