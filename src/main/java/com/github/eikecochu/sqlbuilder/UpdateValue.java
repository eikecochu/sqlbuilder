package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * The UpdateValue class that represents a database value to be used with an
 * UPDATE statement.
 */
@ToString
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class UpdateValue {

	private final Update update;
	private final String column;
	private Object value;
	private boolean expression = false;

	protected UpdateValue(final Update update, final String column) {
		this.update = update;
		this.column = column;
	}

	/**
	 * Set the updated value
	 *
	 * @param value The updated value
	 * @return The UPDATE statement
	 */
	public Update value(final Object value) {
		this.value = value;
		return update;
	}

	/**
	 * Use an expression to update the value
	 *
	 * @param expr The update expression
	 * @return The UPDATE statement
	 */
	public Update expr(final String expr) {
		value = expr;
		expression = true;
		return update;
	}

}