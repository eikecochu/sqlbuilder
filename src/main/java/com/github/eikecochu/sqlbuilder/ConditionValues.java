package com.github.eikecochu.sqlbuilder;

import java.util.ArrayList;
import java.util.List;

import com.github.eikecochu.sqlbuilder.ConditionPart.Operator;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class ConditionValues<T extends Conditionable<T>> {

	private final ConditionPart<T> part;
	private final Operator operator;
	private Object[] values;

	protected ConditionValues(final ConditionPart<T> part, final Operator operator) {
		this.part = part;
		this.operator = operator;
	}

	/**
	 * Set multiple values to be used as conditions
	 *
	 * @param values The condition values
	 * @return This instance
	 */
	public T values(final Object... values) {
		this.values = values;
		return part.condition(this);
	}

	/**
	 * Set multiple values to be used as conditions
	 *
	 * @param values The condition values
	 * @return This instance
	 */
	public T values(final Iterable<Object> values) {
		final List<Object> v = new ArrayList<>();
		for (final Object o : values)
			v.add(o);
		this.values = v.toArray(new Object[v.size()]);
		return part.condition(this);
	}

}
