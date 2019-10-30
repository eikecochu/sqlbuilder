package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

/**
 * Condition to multiple statements, for example WHERE, JOIN etc.
 */
@ToString
@Getter(AccessLevel.PROTECTED)
public class Condition extends Conditionable<Condition> implements QueryPart {

	@Override
	public String string(final QueryOptions options) {
		String condition = super.string(options);
		if (condition == null)
			return null;
		condition = condition.trim();
		if (condition.isEmpty())
			return null;
		return "(" + condition + ")";
	}

}
