package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter(AccessLevel.PROTECTED)
public enum CompareOperator implements QueryPart {
	EQUALS("="),
	NOT_EQUALS("<>"),
	LIKE("LIKE"),
	IN("IN"),
	IS_NULL("IS NULL"),
	IS_NOT_NULL("IS NOT NULL"),
	GE(">="),
	GT(">"),
	LE("<="),
	LT("<="),
	BETWEEN("BETWEEN");

	private final String string;

	CompareOperator(final String string) {
		this.string = string;
	}

	@Override
	public String string(final QueryOptions options) {
		return options.cased(string);
	}

	public static CompareOperator fromString(final String str) {
		for (final CompareOperator op : CompareOperator.values())
			if (op.string.equalsIgnoreCase(str))
				return op;
		throw new RuntimeException("Unknown operator: " + str);
	}
}
