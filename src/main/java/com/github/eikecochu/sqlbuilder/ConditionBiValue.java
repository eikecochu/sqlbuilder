package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter(AccessLevel.PROTECTED)
public class ConditionBiValue<T extends Conditionable<T>> {

	private final ConditionValue<T> value;

	protected ConditionBiValue(final ConditionPart<T> part, final CompareOperator operator) {
		this.value = new ConditionValue<>(part, operator);
	}

	/**
	 * Set two values to be used as conditions
	 *
	 * @param value1 The first value
	 * @param value2 The second value
	 * @return This instance
	 */
	public T values(final Object value1, final Object value2) {
		return value.values(value1, value2);
	}

}