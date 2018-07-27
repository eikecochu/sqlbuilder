package de.ec.sql;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class ConditionValues<T extends Conditionable<T>> {

	private final ConditionPart<T> part;
	private Object[] values;

	protected ConditionValues(ConditionPart<T> part) {
		this.part = part;
	}

	public T values(Object... values) {
		this.values = values;
		return part.condition(this);
	}

}
