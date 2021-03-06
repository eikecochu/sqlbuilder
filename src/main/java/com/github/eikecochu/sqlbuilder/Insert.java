package com.github.eikecochu.sqlbuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * The INSERT expression. Start with this expression to prepare an INSERT query
 * to insert elements into the database.
 */
@ToString
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Insert extends QueryPartImpl<Insert> implements QueryBuilder<Insert>, BeforeSelect<Insert> {

	private final String table;
	private InsertMode insertType = InsertMode.INSERT;
	private final List<InsertValue> insertValues = new ArrayList<>();
	private boolean defaultValues = false;
	private boolean onlyValues = true;

	/**
	 * Create a new INSERT statement
	 *
	 * @param table The name of the table to insert into
	 */
	public Insert(final String table) {
		this(null, table);
	}

	/**
	 * Create a new INSERT statement
	 *
	 * @param table The name of the table to insert into
	 */
	public Insert(final Table table) {
		this(table.tableName());
	}

	protected Insert(final BeforeInsert<?> parent, final String table) {
		super(parent);
		this.table = table;
	}

	/**
	 * Set this INSERT statement to be REPLACE
	 *
	 * @return This INSERT statement
	 */
	public Insert replace() {
		return insertType(InsertMode.REPLACE);
	}

	/**
	 * Set this INSERT statement to be INSERT OR REPLACE
	 *
	 * @return This INSERT statement
	 */
	public Insert orReplace() {
		return insertType(InsertMode.INSERT_OR_REPLACE);
	}

	/**
	 * Set this INSERT statement to be INSERT OR ROLLBACK
	 *
	 * @return This INSERT statement
	 */
	public Insert orRollback() {
		return insertType(InsertMode.INSERT_OR_ROLLBACK);
	}

	/**
	 * Set this INSERT statement to be INSERT OR ABORT
	 *
	 * @return This INSERT statement
	 */
	public Insert orAbort() {
		return insertType(InsertMode.INSERT_OR_ABORT);
	}

	/**
	 * Set this INSERT statement to be INSERT OR FAIL
	 *
	 * @return This INSERT statement
	 */
	public Insert orFail() {
		return insertType(InsertMode.INSERT_OR_FAIL);
	}

	/**
	 * Set this INSERT statement to be INSERT OR IGNORE
	 *
	 * @return This INSERT statement
	 */
	public Insert orIgnore() {
		return insertType(InsertMode.INSERT_OR_IGNORE);
	}

	@Override
	public Select select() {
		insertValues.clear();
		defaultValues = false;
		return BeforeSelect.super.select();
	}

	/**
	 * Use a column to insert values into
	 *
	 * @param column The name of the column
	 * @return The InsertValue instance to set the value to be inserted
	 */
	public InsertValue column(final String column) {
		defaultValues = false;
		onlyValues = false;
		final InsertValue insertValue = new InsertValue(this, column);
		insertValues.add(insertValue);
		return insertValue;
	}

	/**
	 * Insert a value into a column by name
	 *
	 * @param column The name of the column
	 * @param value  The value to be inserted
	 * @return This INSERT statement
	 */
	public Insert column(final String column, final Object value) {
		return column(column).value(value);
	}

	/**
	 * Insert multiple values by ValueHolder
	 *
	 * @param values The ValueHolder of the values to be inserted
	 * @return This INSERT statement
	 */
	public Insert columns(final ValueHolder values) {
		for (final Entry<String, Object> entry : values)
			column(entry.getKey(), entry.getValue());
		return this;
	}

	/**
	 * Insert a value by value only. This will only work if no column names are
	 * specified.
	 *
	 * @param value The value to be inserted
	 * @return This INSERT statement
	 */
	public Insert value(final Object value) {
		return column(null).value(value);
	}

	/**
	 * Insert multiple values by value only. This will only work if no column names
	 * are specified.
	 *
	 * @param values The values to be inserted
	 * @return This INSERT statement
	 */
	public Insert values(final ValueHolder values) {
		for (final Entry<String, Object> entry : values)
			value(entry.getValue());
		return this;
	}

	/**
	 * Resets this INSERT statement columns and values
	 *
	 * @return This INSERT statement
	 */
	public Insert defaults() {
		insertValues.clear();
		defaultValues = true;
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
			strings.add(options.padCased(insertType.string(options)));

			strings.add(" ");
			strings.add(table);

			if (defaultValues)
				strings.add("DEFAULT VALUES");
			else {
				if (!onlyValues) {
					strings.add(" (");

					final StringJoiner columns = new StringJoiner();
					for (final InsertValue insertValue : insertValues)
						columns.add(QueryUtils.splitName(options, insertValue.getColumn())
								.string(options));
					strings.add(columns.toString(", "));

					strings.add(")");
				}

				strings.add(options.newLine());
				strings.add(options.padCased("VALUES"));

				final int count = insertValues.get(0)
						.getValues()
						.size();
				for (int i = 0; i < count; i++) {
					if (i > 0)
						strings.add(options.newLine())
								.add(options.padCased(""));
					strings.add(" (");
					final StringJoiner values = new StringJoiner();
					for (final InsertValue insertValue : insertValues)
						if (options.prepare()) {
							values.add("?");
							options.addPreparedValue(insertValue.getValues()
									.get(i));
						} else
							values.add(QueryUtils.valueToString(options, insertValue.getValues()
									.get(i)));
					strings.add(values.toString(", "));
					strings.add(")");
				}
			}
		}

		return strings.toString();
	}

}
