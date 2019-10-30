package com.github.eikecochu.sqlbuilder;

import com.github.eikecochu.sqlbuilder.ConditionPart.Operator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter(AccessLevel.PROTECTED)
public class ConditionBiValue<T extends Conditionable<T>> {

	private final ConditionPart<T> part;
	private final Operator operator;
	private Object value1;
	private Object value2;

	protected ConditionBiValue(final ConditionPart<T> part, final Operator operator) {
		this.part = part;
		this.operator = operator;
	}

	/**
	 * Set multiple values to be used as conditions
	 *
	 * @param values The condition values
	 * @return This instance
	 */
	public T values(final Object value1, final Object value2) {
		this.value1 = value1;
		this.value2 = value2;
		return part.condition(this);
	}

	public Object[] getValues() {
		return new Object[] { value1, value2 };
	}

}
