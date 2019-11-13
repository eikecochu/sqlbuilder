package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

/**
 * The CompareOperator represents valid comparation operations between SQL
 * expressions, for example in the WHERE statement, or the JOIN statement.
 */
@ToString
@Getter(AccessLevel.PROTECTED)
public enum CompareOperator implements QueryPart {
	/**
	 * The EQUALS operator. Compares for equality.
	 */
	EQUALS("="),

	/**
	 * The NOT EQUALS operator. Compares for inequality.
	 */
	NOT_EQUALS("<>"),

	/**
	 * The LIKE operator. Compares to matching patterns with the % wildcard.
	 */
	LIKE("LIKE"),

	/**
	 * The IN operator. Compares to lists of values.
	 */
	IN("IN"),

	/**
	 * The IS NULL operator. Compares to NULL
	 */
	IS_NULL("IS NULL"),

	/**
	 * The IS NOT NULL operator. Compares to NOT NULL
	 */
	IS_NOT_NULL("IS NOT NULL"),

	/**
	 * The GREATER EQUALS operator. Compares to greater or equal.
	 */
	GE(">="),

	/**
	 * The GREATER THAN operator. Compares to greater.
	 */
	GT(">"),

	/**
	 * The LESSER EQUALS operator. Compares to less or equal.
	 */
	LE("<="),

	/**
	 * The LESS THAN operator. Compares to less.
	 */
	LT("<="),

	/**
	 * The BETWEEN operator. Compares to be in between two values.
	 */
	BETWEEN("BETWEEN");

	private final String string;

	CompareOperator(final String string) {
		this.string = string;
	}

	@Override
	public String string(final QueryOptions options) {
		return options.cased(string);
	}

	/**
	 * Converts a string to a CompareOperator, if possible.
	 *
	 * @param str The operator string
	 * @return the found CompareOperator
	 */
	public static CompareOperator fromString(final String str) {
		for (final CompareOperator op : CompareOperator.values())
			if (op.string.equalsIgnoreCase(str) || op.name()
					.equalsIgnoreCase(str))
				return op;
		throw new RuntimeException("Unknown operator: " + str);
	}
}
