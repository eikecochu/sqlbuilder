package com.github.eikecochu.sqlbuilder;

import java.util.Arrays;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter(AccessLevel.PROTECTED)
public class ConditionPart<T extends Conditionable<T>> implements QueryPart {

	private final Conditionable<T> conditionable;
	private final String name;
	private Object[] values;
	private CompareOperator operator;
	private boolean not;
	private ConditionValueType type;

	protected ConditionPart(final Conditionable<T> conditionable, final String name) {
		this.conditionable = conditionable;
		this.name = name;
	}

	/**
	 * The NOT operator
	 *
	 * @return The value instance to set the compare value
	 */
	public ConditionPart<T> not() {
		this.not = !this.not;
		return this;
	}

	/**
	 * The EQUALS operator
	 *
	 * @return The value instance to set the compare value
	 */
	public ConditionValue<T> eq() {
		return new ConditionValue<>(this, CompareOperator.EQUALS);
	}

	/**
	 * The EQUALS operator. Only accepts values
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T eq(final Object value) {
		return condition(CompareOperator.EQUALS, ConditionValueType.VALUE, value);
	}

	/**
	 * Set a column to be constrained
	 *
	 * @param column The column name
	 * @return This instance
	 */
	public T eqCol(final String column) {
		return eq().col(column);
	}

	/**
	 * Set an expression to be used as condition
	 *
	 * @param expr   The condition expression
	 * @param values Additional values that are passed to the PreparedStatement.
	 *               These values are ignored if the target is not a PreparedQuery.
	 * @return This instance
	 */
	public T eqExpr(final String expr, final Object... values) {
		return eq().expr(expr, values);
	}

	/**
	 * Set an expression to be used as condition
	 *
	 * @param expression The condition expression
	 * @return This instance
	 */
	public T eqExpr(final Expression expression) {
		return eq().expr(expression);
	}

	/**
	 * The NOT EQUALS operator
	 *
	 * @return The value instance to set the compare value
	 */
	public ConditionValue<T> notEq() {
		return new ConditionValue<>(this, CompareOperator.NOT_EQUALS);
	}

	/**
	 * The NOT EQUALS operator. Only accepts values
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T notEq(final Object value) {
		return condition(CompareOperator.NOT_EQUALS, ConditionValueType.VALUE, value);
	}

	/**
	 * The GREATER EQUALS operator
	 *
	 * @return The value instance to set the compare value
	 */
	public ConditionValue<T> ge() {
		return new ConditionValue<>(this, CompareOperator.GE);
	}

	/**
	 * The GREATER EQUALS operator. Only accepts values
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T ge(final Object value) {
		return condition(CompareOperator.GE, ConditionValueType.VALUE, value);
	}

	/**
	 * The GREATER THAN operator
	 *
	 * @return The value instance to set the compare value
	 */
	public ConditionValue<T> gt() {
		return new ConditionValue<>(this, CompareOperator.GT);
	}

	/**
	 * The GREATER THAN operator. Only accepts values
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T gt(final Object value) {
		return condition(CompareOperator.GT, ConditionValueType.VALUE, value);
	}

	/**
	 * The LESSER EQUALS operator
	 *
	 * @return The value instance to set the compare value
	 */
	public ConditionValue<T> le() {
		return new ConditionValue<>(this, CompareOperator.LE);
	}

	/**
	 * The LESSER EQUALS operator. Only accepts values
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T le(final Object value) {
		return condition(CompareOperator.LE, ConditionValueType.VALUE, value);
	}

	/**
	 * The LESSER THAN operator
	 *
	 * @return The value instance to set the compare value
	 */
	public ConditionValue<T> lt() {
		return new ConditionValue<>(this, CompareOperator.LT);
	}

	/**
	 * The LESSER THAN operator. Only accepts values
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T lt(final Object value) {
		return condition(CompareOperator.LT, ConditionValueType.VALUE, value);
	}

	/**
	 * The LIKE operator
	 *
	 * @return The value instance to set the compare value
	 */
	public ConditionValue<T> like() {
		return new ConditionValue<>(this, CompareOperator.LIKE);
	}

	/**
	 * The LIKE operator. Only accepts values
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T like(final Object value) {
		return condition(CompareOperator.LIKE, ConditionValueType.VALUE, value);
	}

	/**
	 * The IN operator
	 *
	 * @return The value instance to set the compare values
	 */
	public ConditionValue<T> in() {
		return new ConditionValue<>(this, CompareOperator.IN);
	}

	/**
	 * The IN operator. Only accepts values
	 *
	 * @param values The values to compare to
	 * @return The previous instance
	 */
	public T in(final Object... values) {
		return in().values(values);
	}

	/**
	 * The IN operator. Only accepts values
	 *
	 * @param values The values to compare to
	 * @return The previous instance
	 */
	public T in(final Iterable<Object> values) {
		return in().values(values);
	}

	/**
	 * The BETWEEN operator
	 *
	 * @return The value instance to set the compare values
	 */
	public ConditionBiValue<T> between() {
		return new ConditionBiValue<>(this, CompareOperator.BETWEEN);
	}

	/**
	 * The BETWEEN operator
	 *
	 * @param value1 The first value
	 * @param value2 The second value
	 * @return The previous instance
	 */
	public T between(final Object value1, final Object value2) {
		return between().values(value1, value2);
	}

	/**
	 * The IS NULL operator
	 *
	 * @return The previous instance
	 */
	public T isNull() {
		return condition(CompareOperator.IS_NULL, ConditionValueType.VALUE, null);
	}

	protected T condition(final CompareOperator operator, final ConditionValueType type, final Object value) {
		return condition(operator, type, value == null ? null : new Object[] { value });
	}

	@SuppressWarnings("unchecked")
	protected T condition(final CompareOperator operator, final ConditionValueType type, final Object[] values) {
		this.values = values;
		this.operator = operator;
		this.type = type;
		return (T) conditionable;
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (operator != CompareOperator.IS_NULL && operator != CompareOperator.IS_NOT_NULL && !options.ignoreNull()
				&& (values == null || values.length == 0))
			operator = CompareOperator.IS_NULL;

		if ((values != null && values.length > 0) || operator == CompareOperator.IS_NULL
				|| operator == CompareOperator.IS_NOT_NULL) {

			if (not) {
				strings.add(options.cased("NOT"));
				strings.add(" ");
			}

			if (name != null) {
				strings.add(QueryUtils.splitName(options, name)
						.string(options));
				strings.add(" ");
			}

			if (operator != null)
				strings.add(operator.string(options));

			if (operator != CompareOperator.IS_NULL && operator != CompareOperator.IS_NOT_NULL) {
				strings.add(" ");

				if (operator == CompareOperator.BETWEEN) {
					if (options.prepare()) {
						strings.add("? AND ?");
						options.addPreparedValue(values[0]);
						options.addPreparedValue(values[1]);
					} else {
						strings.add(QueryUtils.valueToString(options, values[0]));
						strings.add(" AND ");
						strings.add(QueryUtils.valueToString(options, values[1]));
					}
				} else if (operator == CompareOperator.IN) {
					strings.add("(");
					if (options.prepare()) {
						strings.add(QueryUtils.preparedInValues(values.length));
						for (final Object value : values)
							options.addPreparedValue(value);
					} else
						strings.add(StringUtils.join(QueryUtils.valuesToStrings(options, values), ", "));
					strings.add(")");
				} else if (type != null)
					switch (type) {
					case EXPRESSION:
						Expression expression;
						if (values.length == 1)
							expression = new Expression(values[0].toString());
						else {
							final Object[] subValues = Arrays.copyOfRange(values, 1, values.length);
							expression = new Expression(values[0].toString(), subValues);
						}
						strings.add(expression.string(options));
						break;
					case COLUMN:
						strings.add(QueryUtils.splitName(options, values[0].toString())
								.string(options));
						break;
					case ALL:
					case ANY:
						strings.add(type.string(options));
						strings.add(" (");

						final QueryOptions subOptions = options.copy()
								.indentLevel(options.indentLevel() + 1);
						strings.add(subOptions.newLine(true));
						strings.add(((QueryBuilder<?>) values[0]).string(subOptions)
								.trim());

						strings.add(")");
						break;
					case VALUE:
					default:
						if (options.prepare()) {
							strings.add("?");
							options.addPreparedValue(values[0]);
						} else
							strings.add(QueryUtils.valueToString(options, values[0]));
						break;

					}
			}
		}

		return strings.toString();
	}

}
