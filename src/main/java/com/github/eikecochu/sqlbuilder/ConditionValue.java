package com.github.eikecochu.sqlbuilder;

import com.github.eikecochu.sqlbuilder.ConditionPart.Operator;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class ConditionValue<T extends Conditionable<T>> {

	private final ConditionPart<T> part;
	private final Operator operator;
	private Object value;
	private boolean column = false;
	private boolean expression = false;

	protected ConditionValue(final ConditionPart<T> part, final Operator operator) {
		this.part = part;
		this.operator = operator;
	}

	/**
	 * Set a value to be used as condition
	 * @param value The condition value
	 * @return This instance
	 */
	public T value(final Object value) {
		return op(value, false, false);
	}

	/**
	 * Set a column to be constrained
	 * @param column The column name
	 * @return This instance
	 */
	public T col(final String column) {
		return op(column, true, false);
	}

	/**
	 * Set an expression to be used as condition
	 * @param value The condition expression
	 * @return This instance
	 */
	public T expr(final String expr) {
		return op(expr, false, true);
	}

	private T op(final Object value, final boolean column, final boolean expr) {
		this.value = value;
		this.column = column;
		this.expression = expr;
		return part.condition(this);
	}

}
