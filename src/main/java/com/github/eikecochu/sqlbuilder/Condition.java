package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Condition to multiple statements, for example WHERE, JOIN etc.
 */
@Getter(AccessLevel.PROTECTED)
public class Condition extends Conditionable<Condition> implements QueryPart {

	@Override
	public String string(final QueryOptions options) {
		final String condition = super.string(options);
		return condition == null || condition.isEmpty() ? "" : "(" + condition + ")";
	}

}
