package com.github.eikecochu.sqlbuilder;

import com.github.eikecochu.sqlbuilder.ConditionPart.Operator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter(AccessLevel.PROTECTED)
public class ConditionValue<T extends Conditionable<T>> {

	@ToString
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

	private final ConditionPart<T> part;
	private final Operator operator;
	private Object value;
	private ConditionValueType type;

	protected ConditionValue(final ConditionPart<T> part, final Operator operator) {
		this.part = part;
		this.operator = operator;
	}

	/**
	 * Set a value to be used as condition
	 *
	 * @param value The condition value
	 * @return This instance
	 */
	public T value(final Object value) {
		return op(value, ConditionValueType.VALUE);
	}

	/**
	 * Set a column to be constrained
	 *
	 * @param column The column name
	 * @return This instance
	 */
	public T col(final String column) {
		return op(column, ConditionValueType.COLUMN);
	}

	/**
	 * Set an expression to be used as condition
	 *
	 * @param expr The condition expression
	 * @return This instance
	 */
	public T expr(final String expr) {
		return op(expr, ConditionValueType.EXPRESSION);
	}

	/**
	 * Set a subquery to be used as condition with restriction ALL
	 *
	 * @param builder The subquery builder
	 * @return This instance
	 */
	public T all(final QueryBuilder builder) {
		return op(builder, ConditionValueType.ALL);
	}

	/**
	 * Set a subquery to be used as condition with restriction ANY
	 *
	 * @param builder The subquery builder
	 * @return This instance
	 */
	public T any(final QueryBuilder builder) {
		return op(builder, ConditionValueType.ANY);
	}

	private T op(final Object value, final ConditionValueType type) {
		this.value = value;
		this.type = type;
		return part.condition(this);
	}

}
