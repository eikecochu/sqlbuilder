package com.github.eikecochu.sqlbuilder;

/**
 * Implemented by keywords that precede the INSERT statement.
 */
public interface BeforeInsert extends QueryPart {

	/**
	 * Continue query with INSERT
	 *
	 * @param table The name of the table to insert to
	 * @return The new INSERT statement
	 */
	default Insert insert(final String table) {
		return new Insert(this, table);
	}

	/**
	 * Continue query with INSERT
	 *
	 * @param table The Table representation of the table to insert to
	 * @return The new INSERT statement
	 */
	default Insert insert(final Table table) {
		return insert(table.tableName());
	}

	/**
	 * Accept an existing INSERT statement as predecessor
	 *
	 * @param insert The existing INSERT statement
	 * @return Returns the passed INSERT statement
	 */
	default Insert insert(final Insert insert) {
		return insert.parent(this);
	}

	/**
	 * Use plain SQL to form this INSERT statement
	 *
	 * @param sql The sql string
	 * @return The new INSERT statement
	 */
	default Insert insertSQL(final String sql) {
		return insert((String) null).sql(sql);
	}

}
