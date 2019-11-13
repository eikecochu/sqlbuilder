package com.github.eikecochu.sqlbuilder;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

/**
 * The condition value that is used to represent values to use for conditions.
 *
 * @param <T> the type of the conditionable to return to
 */
@ToString
@Getter(AccessLevel.PROTECTED)
public class ConditionValue<T extends Conditionable<T>> {

	private final ConditionPart<T> part;
	private final CompareOperator operator;

	protected ConditionValue(final ConditionPart<T> part, final CompareOperator operator) {
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
		return values(value);
	}

	/**
	 * Set multiple values to be used as conditions
	 *
	 * @param values The condition values
	 * @return This instance
	 */
	public T values(final Object... values) {
		return op(ConditionValueType.VALUE, values);
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
			if (o != null)
				v.add(o);
		return values(v.toArray(new Object[0]));
	}

	/**
	 * Set a column to be constrained
	 *
	 * @param column The column name
	 * @return This instance
	 */
	public T col(final String column) {
		return op(ConditionValueType.COLUMN, column);
	}

	/**
	 * Set an expression to be used as condition
	 *
	 * @param expr   The condition expression
	 * @param values Additional values that are passed to the PreparedStatement.
	 *               These values are ignored if the target is not a PreparedQuery.
	 * @return This instance
	 */
	public T expr(final String expr, final Object... values) {
		return op(ConditionValueType.EXPRESSION, concatenate(expr, values));
	}

	/**
	 * Set an expression to be used as condition
	 *
	 * @param expression The expression object
	 * @return This instance
	 */
	public T expr(final Expression expression) {
		if (expression.values() == null)
			return expr(expression.expression());
		return expr(expression.expression(), expression.values()
				.toArray());
	}

	/**
	 * Set a subquery to be used as condition with restriction ALL
	 *
	 * @param parent The subquery builder
	 * @return This instance
	 */
	public T all(final QueryBuilder<?> parent) {
		return op(ConditionValueType.ALL, parent);
	}

	/**
	 * Set a subquery to be used as condition with restriction ANY
	 *
	 * @param parent The subquery builder
	 * @return This instanceF
	 */
	public T any(final QueryBuilder<?> parent) {
		return op(ConditionValueType.ANY, parent);
	}

	private T op(final ConditionValueType type, final Object... values) {
		return part.condition(operator, type, removeNull(values));
	}

	private Object[] concatenate(final Object a, final Object[] b) {
		if (b == null || b.length == 0)
			return a == null ? null : new Object[] { a };
		if (a == null)
			return b;
		final int bLen = b.length;
		final Object[] c = new Object[1 + bLen];
		c[0] = a;
		System.arraycopy(b, 0, c, 1, bLen);
		return c;
	}

	private Object[] removeNull(final Object[] a) {
		if (a == null || a.length == 0)
			return null;
		final List<Object> nonNull = new ArrayList<>(a.length);
		for (final Object o : a)
			if (o != null)
				nonNull.add(o);
		if (nonNull.size() == a.length)
			return a;
		return nonNull.toArray();
	}

}
