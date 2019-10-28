package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class Condition extends Conditionable<Condition> implements QueryPart {

	@Override
	public String string(final QueryOptions options) {
		return "(" + super.string(options) + ")";
	}

}
