package com.github.eikecochu.sqlbuilder;

import org.apache.commons.lang3.StringUtils;

import com.github.eikecochu.sqlbuilder.ConditionValue.ConditionValueType;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class ConditionPart<T extends Conditionable<T>> implements QueryPart {

	public enum Operator implements QueryPart {
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
		this.not = true;
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
	 * The NOT EQUALS operator
	 *
	 * @return The value instance to set the compare value
	 */
	public ConditionValue<T> notEq() {
		return new ConditionValue<>(this, Operator.NOT_EQUALS);
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
	 * The GREATER THAN operator
	 *
	 * @return The value instance to set the compare value
	 */
	public ConditionValue<T> gt() {
		return new ConditionValue<>(this, Operator.GT);
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
	 * The LESSER THAN operator
	 *
	 * @return The value instance to set the compare value
	 */
	public ConditionValue<T> lt() {
		return new ConditionValue<>(this, Operator.LT);
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
	 * The IN operator
	 *
	 * @return The value instance to set the compare values
	 */
	public ConditionValues<T> in() {
		return new ConditionValues<>(this, Operator.IN);
	}

	/**
	 * The BETWEEN operator
	 *
	 * @return The value instance to set the compare values
	 */
	public ConditionBiValue<T> between() {
		return new ConditionBiValue<>(this, Operator.BETWEEN);
	}

	@SuppressWarnings("unchecked")
	protected T condition(final ConditionValue<T> conditionValue) {
		this.operator = conditionValue.getOperator();
		this.value = conditionValue.getValue();
		this.type = conditionValue.getType();
		return (T) conditionable;
	}

	@SuppressWarnings("unchecked")
	protected T condition(final ConditionValues<T> conditionValues) {
		this.values = conditionValues.getValues();
		this.operator = conditionValues.getOperator();
		return (T) conditionable;
	}

	@SuppressWarnings("unchecked")
	protected T condition(final ConditionBiValue<T> conditionValues) {
		this.values = conditionValues.getValues();
		this.operator = conditionValues.getOperator();
		return (T) conditionable;
	}

	/**
	 * The EQUALS operator
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T eq(final Object value) {
		return op(value, Operator.EQUALS);
	}

	/**
	 * The GREATER EQUALS operator
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T ge(final Object value) {
		return op(value, Operator.GE);
	}

	/**
	 * The GREATER THAN operator
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T gt(final Object value) {
		return op(value, Operator.GT);
	}

	/**
	 * The LESSER EQUALS operator
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T le(final Object value) {
		return op(value, Operator.LE);
	}

	/**
	 * The LESSER THAN operator
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T lt(final Object value) {
		return op(value, Operator.LT);
	}

	/**
	 * The LIKE operator
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T like(final Object value) {
		return op(value, Operator.LIKE);
	}

	/**
	 * The IS NULL operator
	 *
	 * @param value The value to compare to
	 * @return The previous instance
	 */
	public T isNull() {
		return op(null, Operator.IS_NULL);
	}

	/**
	 * The IN operator
	 *
	 * @param values The values to compare to
	 * @return The previous instance
	 */
	public T in(final Object... values) {
		return in().values(values);
	}

	/**
	 * The IN operator
	 *
	 * @param values The values to compare to
	 * @return The previous instance
	 */
	public T in(final Iterable<Object> values) {
		return in().values(values);
	}

	@SuppressWarnings("unchecked")
	protected T op(final Object value, final Operator operator) {
		this.value = value;
		this.operator = operator;
		this.type = ConditionValueType.VALUE;
		return (T) conditionable;
	}

	@Override
	public String string(final QueryOptions options) {
		if (value != null || (values != null && values.length > 0) || operator == Operator.IS_NULL
				|| operator == Operator.IS_NOT_NULL) {
			final StringJoiner strings = new StringJoiner();
			if (not && operator == Operator.IS_NULL) {
				not = false;
				operator = Operator.IS_NOT_NULL;
			}

			if (not) {
				strings.add(options.cased("NOT"));
				strings.add(" ");
			}

			strings.add(QueryUtils.splitName(options, name)
					.string(options));
			strings.add(" ");

			strings.add(operator.string(options));

			if (operator != Operator.IS_NULL && operator != Operator.IS_NOT_NULL) {
				strings.add(" ");
				if (type != null) {
					if (type == ConditionValueType.EXPRESSION)
						strings.add(QueryUtils.valueToString(options, value));
					else if (type == ConditionValueType.COLUMN)
						strings.add(QueryUtils.splitName(options, value.toString())
								.string(options));
					else if (type == ConditionValueType.VALUE) {
						if (options.prepare()) {
							strings.add("?");
							options.addPreparedValue(value);
						} else
							strings.add(QueryUtils.valueToString(options, value));
					} else if (type == ConditionValueType.ALL || type == ConditionValueType.ANY) {
						strings.add(type.toString());
						strings.add(" (");

						final QueryOptions subOptions = options.copy()
								.indentLevel(options.indentLevel() + 1);
						strings.add(subOptions.newLine(true));
						strings.add(((QueryBuilder) value).string(subOptions)
								.trim());

						strings.add(")");
					}
				} else if (operator == Operator.BETWEEN) {
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

			return strings.toString();
		}

		return "";
	}

}
