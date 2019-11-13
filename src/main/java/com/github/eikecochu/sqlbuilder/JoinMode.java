package com.github.eikecochu.sqlbuilder;

import lombok.ToString;

/**
 * The JoinMode describes how a JOIN between two elements is made.
 */
@ToString
public enum JoinMode implements QueryPart {
	/**
	 * The INNER JOIN mode. Default mode. Will join only if both sides have matching
	 * values.
	 */
	INNER_JOIN("INNER JOIN"),

	/**
	 * The OUTER JOIN mode. Also FULL OUTER JOIN mode. Will join two elements
	 * regardless of matching values.
	 */
	OUTER_JOIN("OUTER JOIN"),

	/**
	 * The LEFT JOIN mode. Will join two elements if both sides have matching values
	 * or only the left side has values.
	 */
	LEFT_JOIN("LEFT JOIN"),

	/**
	 * The RIGHT JOIN mode. Will join two elements if both sides have matching
	 * values or only the right side has values.
	 */
	RIGHT_JOIN("RIGHT JOIN"),

	/**
	 * The CROSS JOIN mode. Will join two elements for every possible value
	 * combination.
	 */
	CROSS_JOIN("CROSS JOIN");

	private final String string;

	JoinMode(final String string) {
		this.string = string;
	}

	@Override
	public String string(final QueryOptions options) {
		return options.cased(string);
	}
}