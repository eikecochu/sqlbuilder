package com.github.eikecochu.sqlbuilder;

/**
 * Implemented by keywords that precede the UPDATE statement.
 */
public interface BeforeUpdate extends QueryPart {

	/**
	 * Continue query with UPDATE
	 *
	 * @param table The name of table to be updated
	 * @return The new UPDATE statement
	 */
	default Update update(final String table) {
		return new Update(this, table);
	}

	/**
	 * Continue query with UPDATE
	 *
	 * @param table The Table representation of table to be updated
	 * @return The new UPDATE statement
	 */
	default Update update(final Table table) {
		return update(table.tableName());
	}

	/**
	 * Accept an existing UPDATE statement as predecessor
	 *
	 * @param update The existing UPDATE statement
	 * @return Returns the passed UPDATE statement
	 */
	default Update update(final Update update) {
		return update.parent(this);
	}

	/**
	 * Use plain SQL to form this UPDATE statement
	 *
	 * @param sql The sql string
	 * @return The new UPDATE statement
	 */
	default Update updateSQL(final String sql) {
		return update((String) null).sql(sql);
	}

}
