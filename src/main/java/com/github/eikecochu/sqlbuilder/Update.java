package com.github.eikecochu.sqlbuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Update extends QueryPartImpl<Update> implements QueryBuilder, BeforeWhere {

	@ToString
	private enum UpdateType implements QueryPart {
		UPDATE("UPDATE"),
		UPDATE_OR_ROLLBACK("UPDATE OR ROLLBACK"),
		UPDATE_OR_ABORT("UPDATE OR ABORT"),
		UPDATE_OR_REPLACE("UPDATE OR REPLACE"),
		UPDATE_OR_FAIL("UPDATE OR FAIL"),
		UPDATE_OR_IGNORE("UPDATE OR IGNORE");

		private final String string;

		UpdateType(final String string) {
			this.string = string;
		}

		@Override
		public String string(final QueryOptions options) {
			return options.cased(string);
		}
	}

	@ToString
	@Getter(AccessLevel.PROTECTED)
	public static class UpdateValue {

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

	private UpdateType updateType = UpdateType.UPDATE;
	private final String table;
	private final List<UpdateValue> updateValues = new ArrayList<>();

	/**
	 * Create a new UPDATE statement
	 *
	 * @param table The name of the table to update
	 */
	public Update(final String table) {
		this(null, table);
	}

	/**
	 * Create a new UPDATE statement
	 *
	 * @param table The Table representation of the table to update
	 */
	public Update(final Table table) {
		this(table.tableName());
	}

	protected Update(final BeforeUpdate parent, final String table) {
		super(parent);
		this.table = table;
	}

	private Update setType(final UpdateType updateType) {
		this.updateType = updateType;
		return this;
	}

	/**
	 * Set this UPDATE statement to be UPDATE OR ROLLBACK
	 *
	 * @return This UPDATE statement
	 */
	public Update orRollback() {
		return setType(UpdateType.UPDATE_OR_ROLLBACK);
	}

	/**
	 * Set this UPDATE statement to be UPDATE OR ABORT
	 *
	 * @return This UPDATE statement
	 */
	public Update orAbort() {
		return setType(UpdateType.UPDATE_OR_ABORT);
	}

	/**
	 * Set this UPDATE statement to be UPDATE OR FAIL
	 *
	 * @return This UPDATE statement
	 */
	public Update orFail() {
		return setType(UpdateType.UPDATE_OR_FAIL);
	}

	/**
	 * Set this UPDATE statement to be UPDATE OR REPLACE
	 *
	 * @return This UPDATE statement
	 */
	public Update orReplace() {
		return setType(UpdateType.UPDATE_OR_REPLACE);
	}

	/**
	 * Set this UPDATE statement to be UPDATE OR IGNORE
	 *
	 * @return This UPDATE statement
	 */
	public Update orIgnore() {
		return setType(UpdateType.UPDATE_OR_IGNORE);
	}

	/**
	 * Set a value to be updated
	 *
	 * @param column The column name to use for updating the value
	 * @return The UpdateValue instance to set the value for updating
	 */
	public UpdateValue set(final String column) {
		final UpdateValue updateValue = new UpdateValue(this, column);
		updateValues.add(updateValue);
		return updateValue;
	}

	/**
	 * Set a value to be updated
	 *
	 * @param column The column name to use for updating the value
	 * @param value  The value to be updated
	 * @return This UPDATE statement
	 */
	public Update set(final String column, final Object value) {
		return set(column).value(value);
	}

	/**
	 * Set multiple values to be updated
	 *
	 * @param values The ValueHolder instance that holds the update values
	 * @return This UPDATE statement
	 */
	public Update set(final ValueHolder values) {
		for (final Entry<String, Object> entry : values)
			set(entry.getKey(), entry.getValue());
		return this;
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (parent() != null)
			strings.add(parent().string(options));

		if (strings.notEmpty())
			strings.add(options.newLine());

		if (sql() != null)
			strings.add(options.padded(sql()));
		else {
			strings.add(options.padCased(updateType.string(options)));

			strings.add(" ");
			strings.add(table);

			strings.add(options.newLine());
			strings.add(options.padCased("SET"));
			strings.add(" ");

			final StringJoiner sets = new StringJoiner();
			for (final UpdateValue updateValue : updateValues) {
				String column = QueryUtils.splitName(options, updateValue.column())
						.string(options) + " = ";
				if (updateValue.expression())
					column += updateValue.value()
							.toString();
				else if (options.prepare()) {
					column += "?";
					options.addPreparedValue(updateValue.value());
				} else
					column += QueryUtils.valueToString(options, updateValue.value());
				sets.add(column);
			}
			strings.add(sets.toString(", "));
		}

		return strings.toString();
	}

}
