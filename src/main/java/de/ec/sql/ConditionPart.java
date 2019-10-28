package de.ec.sql;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class ConditionPart<T extends Conditionable<T>> implements QueryPart {

	public enum Operator implements QueryPart {
		EQUALS("="), LIKE("LIKE"), IN("IN"), IS_NULL("IS NULL"), IS_NOT_NULL("IS NOT NULL"), GE(">="), GT(">"),
		LE("<="), LT("<=");

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
	private boolean column;
	private boolean expression;

	protected ConditionPart(final Conditionable<T> conditionable, final String name) {
		this.conditionable = conditionable;
		this.name = name;
	}

	public ConditionPart<T> not() {
		this.not = true;
		return this;
	}

	public ConditionValue<T> eq() {
		return new ConditionValue<>(this, Operator.EQUALS);
	}

	public ConditionValue<T> ge() {
		return new ConditionValue<>(this, Operator.GE);
	}

	public ConditionValue<T> gt() {
		return new ConditionValue<>(this, Operator.GT);
	}

	public ConditionValue<T> le() {
		return new ConditionValue<>(this, Operator.LE);
	}

	public ConditionValue<T> lt() {
		return new ConditionValue<>(this, Operator.LT);
	}

	public ConditionValue<T> like() {
		return new ConditionValue<>(this, Operator.LIKE);
	}

	public ConditionValues<T> in() {
		return new ConditionValues<>(this);
	}

	@SuppressWarnings("unchecked")
	protected T condition(final ConditionValue<T> conditionValue) {
		this.operator = conditionValue.getOperator();
		this.value = conditionValue.getValue();
		this.column = conditionValue.isColumn();
		this.expression = conditionValue.isExpression();
		return (T) conditionable;
	}

	@SuppressWarnings("unchecked")
	protected T condition(final ConditionValues<T> conditionValues) {
		this.values = conditionValues.getValues();
		this.operator = Operator.IN;
		return (T) conditionable;
	}

	public T eq(final Object value) {
		return op(value, Operator.EQUALS);
	}

	public T ge(final Object value) {
		return op(value, Operator.GE);
	}

	public T gt(final Object value) {
		return op(value, Operator.GT);
	}

	public T le(final Object value) {
		return op(value, Operator.LE);
	}

	public T lt(final Object value) {
		return op(value, Operator.LT);
	}

	public T like(final Object value) {
		return op(value, Operator.LIKE);
	}

	public T isNull() {
		return op(null, Operator.IS_NULL);
	}

	public T in(final Object... values) {
		return in().values(values);
	}

	public T in(final Iterable<Object> values) {
		return in().values(values);
	}

	@SuppressWarnings("unchecked")
	protected T op(final Object value, final Operator operator) {
		this.value = value;
		this.operator = operator;
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
				if (expression)
					strings.add(QueryUtils.valueToString(options, value));
				else if (column)
					strings.add(QueryUtils.splitName(options, value.toString())
							.string(options));
				else if (value != null) {
					if (options.prepare()) {
						strings.add("?");
						options.addPreparedValue(value);
					} else
						strings.add(QueryUtils.valueToString(options, value));
				} else {
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
