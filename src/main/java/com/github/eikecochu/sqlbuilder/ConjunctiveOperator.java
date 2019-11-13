package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

/**
 * The ConjunctiveOperator is used to chain the various ConditionPart elements
 * together to form the conditions of a query.
 */
@ToString
@Getter(AccessLevel.PROTECTED)
public enum ConjunctiveOperator implements QueryPart {
	/**
	 * The AND operator. Will pass if the left and right operator evaluate to true.
	 */
	AND("AND"),

	/**
	 * The OR operator. Will pass if the left or the right operator or both evaluate
	 * to true.
	 */
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