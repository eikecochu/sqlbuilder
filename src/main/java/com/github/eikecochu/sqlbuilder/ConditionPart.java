package com.github.eikecochu.sqlbuilder;

import com.github.eikecochu.sqlbuilder.ConditionValue.ConditionValueType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter(AccessLevel.PROTECTED)
public class ConditionPart<T extends Conditionable<T>> implements QueryPart {

	@ToString
	public static enum Operator implements QueryPart {
		EQUALS("="),
		NOT_EQUALS("<>"),
		LIKE("LIKE"),
		IN("IN"),
		IS_NULL("IS NULL"),
		IS_NOT_NULL("IS NOT NULL"),
		GE(">="),
		GT(">"),
		LE("<="),
		LT("<="),
		BETWEEN("BETWEEN");

		private String string;

		private Operator(final String string) {
			this.string = string;
		}

		@Override
		public String string(final QueryOptions options) {
			return options.cased(string);
		}
	}

	private final Conditionable<T> conditionable;
	private final String name;
	private Object value;
	private Object[] values;
	private Operator operator;
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
		return new ConditionValue<>(this, Operator.EQUALS);
	}

	/**
	 * The EQUALS operator. Only accepts values
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T eq(final Object value) {
		return condition(value, null, Operator.EQUALS, ConditionValueType.VALUE);
	}

	/**
	 * The NOT EQUALS operator
	 *
	 * @return The value instance to set the compare value
	 */
	public ConditionValue<T> notEq() {
		return new ConditionValue<>(this, Operator.NOT_EQUALS);
	}

	/**
	 * The NOT EQUALS operator. Only accepts values
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T notEq(final Object value) {
		return condition(value, null, Operator.NOT_EQUALS, ConditionValueType.VALUE);
	}

	/**
	 * The GREATER EQUALS operator
	 *
	 * @return The value instance to set the compare value
	 */
	public ConditionValue<T> ge() {
		return new ConditionValue<>(this, Operator.GE);
	}

	/**
	 * The GREATER EQUALS operator. Only accepts values
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T ge(final Object value) {
		return condition(value, null, Operator.GE, ConditionValueType.VALUE);
	}

	/**
	 * The GREATER THAN operator
	 *
	 * @return The value instance to set the compare value
	 */
	public ConditionValue<T> gt() {
		return new ConditionValue<>(this, Operator.GT);
	}

	/**
	 * The GREATER THAN operator. Only accepts values
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T gt(final Object value) {
		return condition(value, null, Operator.GT, ConditionValueType.VALUE);
	}

	/**
	 * The LESSER EQUALS operator
	 *
	 * @return The value instance to set the compare value
	 */
	public ConditionValue<T> le() {
		return new ConditionValue<>(this, Operator.LE);
	}

	/**
	 * The LESSER EQUALS operator. Only accepts values
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T le(final Object value) {
		return condition(value, null, Operator.LE, ConditionValueType.VALUE);
	}

	/**
	 * The LESSER THAN operator
	 *
	 * @return The value instance to set the compare value
	 */
	public ConditionValue<T> lt() {
		return new ConditionValue<>(this, Operator.LT);
	}

	/**
	 * The LESSER THAN operator. Only accepts values
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T lt(final Object value) {
		return condition(value, null, Operator.LT, ConditionValueType.VALUE);
	}

	/**
	 * The LIKE operator
	 *
	 * @return The value instance to set the compare value
	 */
	public ConditionValue<T> like() {
		return new ConditionValue<>(this, Operator.LIKE);
	}

	/**
	 * The LIKE operator. Only accepts values
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T like(final Object value) {
		return condition(value, null, Operator.LIKE, ConditionValueType.VALUE);
	}

	/**
	 * The IN operator
	 *
	 * @return The value instance to set the compare values
	 */
	public ConditionValues<T> in() {
		return new ConditionValues<>(this, Operator.IN);
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
		return new ConditionBiValue<>(this, Operator.BETWEEN);
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
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T isNull() {
		return condition(null, null, Operator.IS_NULL, ConditionValueType.VALUE);
	}

	protected T condition(final ConditionValue<T> conditionValue) {
		return condition(conditionValue.getValue(), null, conditionValue.getOperator(), conditionValue.getType());
	}

	protected T condition(final ConditionValues<T> conditionValues) {
		return condition(null, conditionValues.getValues(), conditionValues.getOperator(), null);
	}

	protected T condition(final ConditionBiValue<T> conditionValues) {
		return condition(null, conditionValues.getValues(), conditionValues.getOperator(), null);
	}

	@SuppressWarnings("unchecked")
	protected T condition(final Object value, final Object[] values, final Operator operator,
			final ConditionValueType type) {
		this.value = value;
		this.values = values;
		this.operator = operator;
		this.type = type;
		return (T) conditionable;
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (value != null || (values != null && values.length > 0) || operator == Operator.IS_NULL
				|| operator == Operator.IS_NOT_NULL) {

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

			if (operator != Operator.IS_NULL && operator != Operator.IS_NOT_NULL) {
				strings.add(" ");

				if (type != null)
					switch (type) {
					case EXPRESSION:
						strings.add(QueryUtils.valueToString(options, value));
						break;
					case COLUMN:
						strings.add(QueryUtils.splitName(options, value.toString())
								.string(options));
						break;
					case ALL:
					case ANY:
						strings.add(type.string(options));
						strings.add(" (");

						final QueryOptions subOptions = options.copy()
								.indentLevel(options.indentLevel() + 1);
						strings.add(subOptions.newLine(true));
						strings.add(((QueryBuilder) value).string(subOptions)
								.trim());

						strings.add(")");
						break;
					case VALUE:
					default:
						if (options.prepare()) {
							strings.add("?");
							options.addPreparedValue(value);
						} else
							strings.add(QueryUtils.valueToString(options, value));
						break;

					}
				else if (operator == Operator.BETWEEN) {
					if (options.prepare()) {
						strings.add("? AND ?");
						options.addPreparedValue(values[0]);
						options.addPreparedValue(values[1]);
					} else {
						strings.add(QueryUtils.valueToString(options, values[0]));
						strings.add(" AND ");
						strings.add(QueryUtils.valueToString(options, values[1]));
					}
				} else if (operator == Operator.IN) {
					strings.add("(");
					if (options.prepare()) {
						strings.add(QueryUtils.preparedInValues(values.length));
						for (final Object value : values)
							options.addPreparedValue(value);
					} else
						strings.add(StringUtils.join(QueryUtils.valuesToStrings(options, values), ", "));
					strings.add(")");
				}
			}
		}

		return strings.toString();
	}

}
