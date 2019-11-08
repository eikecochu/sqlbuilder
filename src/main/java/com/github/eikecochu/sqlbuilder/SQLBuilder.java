package com.github.eikecochu.sqlbuilder;

public final class SQLBuilder {

	private SQLBuilder() {
	}

	/**
	 * Start with a WITH statement
	 *
	 * @param name The name of the WITH block
	 * @return The new WITH statement
	 */
	public static With With(final String name) {
		return new With(name);
	}

	/**
	 * Start with a SELECT statement
	 *
	 * @return The new SELECT statement
	 */
	public static Select Select() {
		return new Select();
	}

	/**
	 * Start with a SELECT statement
	 *
	 * @param columns The columns to be selected
	 * @return The new SELECT statement
	 */
	public static Select Select(final String... columns) {
		return new Select(columns);
	}

	/**
	 * Start with an INSERT statement
	 *
	 * @param table The table name of the table to insert into
	 * @return The new INSERT statement
	 */
	public static Insert Insert(final String table) {
		return new Insert(table);
	}

	/**
	 * Start with an INSERT statement
	 *
	 * @param table The Table representation of the table to insert into
	 * @return The new INSERT statement
	 */
	public static Insert Insert(final Table table) {
		return new Insert(table);
	}

	/**
	 * Start with an UPDATE statement
	 *
	 * @param table The table name of the table to update
	 * @return The new UPDATE statement
	 */
	public static Update Update(final String table) {
		return new Update(table);
	}

	/**
	 * Start with an UPDATE statement
	 *
	 * @param table The Table representation of the table to update
	 * @return The new UPDATE statement
	 */
	public static Update Update(final Table table) {
		return new Update(table);
	}

	/**
	 * Start with an DELETE statement
	 *
	 * @param table The table name of the table to delete from
	 * @return The new DELETE statement
	 */
	public static Delete Delete(final String table) {
		return new Delete(table);
	}

	/**
	 * Start with an DELETE statement
	 *
	 * @param table The Table representation of the table to delete from
	 * @return The new DELETE statement
	 */
	public static Delete Delete(final Table table) {
		return new Delete(table);
	}

	/**
	 * Create a final Query
	 *
	 * @param sql The SQL statement to use
	 * @return The new Query
	 */
	public static Query Query(final String sql) {
		return new Query(null).sql(sql);
	}

}
