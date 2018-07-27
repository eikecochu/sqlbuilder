package de.ec.sql;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class UpdateValue {

	private final Update update;
	private final String column;
	private Object value;
	private boolean expression = false;

	protected UpdateValue(Update update, String column) {
		this.update = update;
		this.column = column;
	}

	public Update value(Object value) {
		this.value = value;
		return update;
	}

	public Update expr(String expr) {
		value = expr;
		expression = true;
		return update;
	}

}
