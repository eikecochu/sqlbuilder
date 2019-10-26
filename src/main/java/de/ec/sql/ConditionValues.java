package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class ConditionValues<T extends Conditionable<T>> {

	private final ConditionPart<T> part;
	private Object[] values;

	protected ConditionValues(final ConditionPart<T> part) {
		this.part = part;
	}

	public T values(final Object... values) {
		this.values = values;
		return part.condition(this);
	}

	public T values(final Iterable<Object> values) {
		final List<Object> v = new ArrayList<>();
		for (final Object o : values)
			v.add(o);
		this.values = v.toArray(new Object[v.size()]);
		return part.condition(this);
	}

}
