package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

/**
 * The ConditionValueType is used to express what a ConditionPart contains as
 * value. Based on this type, the output is modified, for example values are
 * escaped.
 */
@ToString
@Getter(AccessLevel.PROTECTED)
public enum ConditionValueType implements QueryPart {
	/**
	 * The VALUE type. Values are literal values.
	 */
	VALUE,

	/**
	 * The COLUMN value. Declares the passed value as a column name.
	 */
	COLUMN,

	/**
	 * The EXPRESSION value. Declares the passed value as an expression.
	 */
	EXPRESSION,

	/**
	 * The ANY value. Special type which adds the ANY expression to the condition.
	 */
	ALL,

	/**
	 * The ALL value. Special type which adds the ALL expression to the condition.
	 */
	ANY;

	@Override
	public String string(final QueryOptions options) {
		if (this == ALL)
			return "ALL";
		if (this == ANY)
			return "ANY";
		return null;
	}
}