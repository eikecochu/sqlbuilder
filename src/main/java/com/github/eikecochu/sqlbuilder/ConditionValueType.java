package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter(AccessLevel.PROTECTED)
public enum ConditionValueType implements QueryPart {
	VALUE,
	COLUMN,
	EXPRESSION,
	ALL,
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