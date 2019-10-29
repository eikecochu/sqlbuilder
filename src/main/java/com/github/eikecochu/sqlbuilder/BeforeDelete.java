package com.github.eikecochu.sqlbuilder;

/**
 * Implemented by keywords that precede the DELETE statement.
 */
public interface BeforeDelete extends QueryPart {

	/**
	 * Continue query with DELETE
	 *
	 * @param table The name of the table to delete from
	 * @return The new DELETE statement
	 */
	default Delete delete(final String table) {
		return new Delete(this, table);
	}

	/**
	 * Continue query with DELETE
	 *
	 * @param table The Table representation of the table to delete from
	 * @return The new DELETE statement
	 */
	default Delete delete(final Table table) {
		return delete(table.tableName());
	}

	/**
	 * Accept an existing DELETE statement as predecessor
	 *
	 * @param delete The existing DELETE statement
	 * @return Returns the passed DELETE statement
	 */
	default Delete delete(final Delete delete) {
		return delete.builder(this);
	}

	/**
	 * Use plain SQL to form this DELETE statement
	 *
	 * @param sql The sql string
	 * @return The new DELETE statement
	 */
	default Delete deleteSQL(final String sql) {
		return delete((String) null).sql(sql);
	}

}
