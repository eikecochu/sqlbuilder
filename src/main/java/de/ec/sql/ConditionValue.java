package de.ec.sql;

import de.ec.sql.ConditionPart.Operator;
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

	public T value(final Object value) {
		return op(value, false, false);
	}

	public T col(final String column) {
		return op(column, true, false);
	}

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
