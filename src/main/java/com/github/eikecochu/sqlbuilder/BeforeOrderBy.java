package com.github.eikecochu.sqlbuilder;

/**
 * Implemented by keywords that precede the ORDER BY statement.
 */
public interface BeforeOrderBy<T extends BeforeOrderBy<T>> extends QueryPart, QueryPartSQL<T>, QueryPartLinked<T> {

	/**
	 * Continue query with ORDER BY
	 *
	 * @return The new ORDER BY statement
	 */
	default OrderBy orderBy() {
		return new OrderBy(this);
	}

	/**
	 * Continue query with ORDER BY
	 *
	 * @param columns The column names to order by
	 * @return The new ORDER BY statement
	 */
	default OrderBy orderBy(final String... columns) {
		return orderBy().columns(columns);
	}

	/**
	 * Accept an existing ORDER BY statement as predecessor
	 *
	 * @param orderBy The existing ORDER BY statement
	 * @return Returns the passed ORDER BY statement
	 */
	default OrderBy orderBy(final OrderBy orderBy) {
		return orderBy.parent(this);
	}

	/**
	 * Use plain SQL to form this ORDER BY statement
	 *
	 * @param sql The sql string
	 * @return The new ORDER BY statement
	 */
	default OrderBy orderBySQL(final String sql) {
		return orderBy().sql(sql);
	}

}
