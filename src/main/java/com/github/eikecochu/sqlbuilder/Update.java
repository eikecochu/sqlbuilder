package com.github.eikecochu.sqlbuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Update extends SQLQueryPart<Update> implements QueryBuilder, BeforeWhere {

	@ToString
	protected enum UpdateType implements QueryPart {
		UPDATE("UPDATE"),
		UPDATE_OR_ROLLBACK("UPDATE OR ROLLBACK"),
		UPDATE_OR_ABORT("UPDATE OR ABORT"),
		UPDATE_OR_REPLACE("UPDATE OR REPLACE"),
		UPDATE_OR_FAIL("UPDATE OR FAIL"),
		UPDATE_OR_IGNORE("UPDATE OR IGNORE");

		private String string;

		private UpdateType(final String string) {
			this.string = string;
		}

		@Override
		public String string(final QueryOptions options) {
			return options.cased(string);
		}
	}

	private UpdateType updateType = UpdateType.UPDATE;
	private BeforeUpdate builder;
	private final String table;
	private final List<UpdateValue> updateValues = new ArrayList<>();

	/**
	 * Create a new UPDATE statement
	 *
	 * @param table The name of the table to update
	 */
	public Update(final String table) {
		this.table = table;
	}

	/**
	 * Create a new UPDATE statement
	 *
	 * @param table The Table representation of the table to update
	 */
	public Update(final Table table) {
		this(table.tableName());
	}

	protected Update(final BeforeUpdate builder, final String table) {
		this(table);
		this.builder = builder;
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

		if (builder != null) {
			strings.add(builder.string(options));
			strings.add(options.newLine());
		}

		if (sql() != null)
			strings.add(sql());
		else {
			strings.add(options.padCased(updateType.string(options)));

			strings.add(" ");
			strings.add(table);

			strings.add(options.newLine());
			strings.add(options.padCased("SET"));
			strings.add(" ");

			final StringJoiner sets = new StringJoiner();
			for (final UpdateValue updateValue : updateValues) {
				String column = QueryUtils.splitName(options, updateValue.getColumn())
						.string(options) + " = ";
				if (updateValue.isExpression())
					column += updateValue.getValue()
							.toString();
				else if (options.prepare()) {
					column += "?";
					options.addPreparedValue(updateValue.getValue());
				} else
					column += QueryUtils.valueToString(options, updateValue.getValue());
				sets.add(column);
			}
			strings.add(sets.toString(", "));
		}

		return strings.toString();
	}

}
