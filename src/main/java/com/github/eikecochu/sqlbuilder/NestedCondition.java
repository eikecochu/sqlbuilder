package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Condition to multiple statements, for example WHERE, JOIN etc.
 */
@Getter(AccessLevel.PROTECTED)
public class NestedCondition<T extends Conditionable<T>> extends Conditionable<NestedCondition<T>>
		implements QueryPart {

	private final Conditionable<T> parent;

	NestedCondition(final Conditionable<T> parent) {
		this.parent = parent;
	}

	@SuppressWarnings("unchecked")
	T groupEnd() {
		return parent == null ? (T) this : (T) parent;
	}

	@Override
	public String string(final QueryOptions options) {
		return "(" + super.string(options) + ")";
	}

}
