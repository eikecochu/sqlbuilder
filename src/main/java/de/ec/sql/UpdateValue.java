package de.ec.sql;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class UpdateValue {

	private final Update update;
	private final String column;
	private Object value;
	private boolean expression = false;

	protected UpdateValue(final Update update, final String column) {
		this.update = update;
		this.column = column;
	}

	public Update value(final Object value) {
		this.value = value;
		return update;
	}

	public Update expr(final String expr) {
		value = expr;
		expression = true;
		return update;
	}

}
