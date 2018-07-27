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

	protected ConditionValue(ConditionPart<T> part, Operator operator) {
		this.part = part;
		this.operator = operator;
	}

	public T value(Object value) {
		return op(value, false, false);
	}

	public T col(String column) {
		return op(column, true, false);
	}

	public T expr(String expr) {
		return op(expr, false, true);
	}

	private T op(Object value, boolean column, boolean expr) {
		this.value = value;
		this.column = column;
		this.expression = expr;
		return part.condition(this);
	}

}
