package com.github.eikecochu.sqlbuilder;

/**
 * Implemented by keywords that precede the HAVING statement.
 *
 * @param <T> the generic type returned by some fluent methods of this interface
 */
public interface BeforeHaving<T extends BeforeHaving<T>> extends QueryPart, QueryPartSQL<T>, QueryPartLinked<T> {

	/**
	 * Continue query with HAVING
	 *
	 * @return The new HAVING statement
	 */
	default Having having() {
		return new Having(this);
	}

	/**
	 * Accept an existing HAVING statement as predecessor
	 *
	 * @param having The existing HAVING statement
	 * @return Returns the passed HAVING statement
	 */
	default Having having(final Having having) {
		return having.parent(this);
	}

	/**
	 * Use plain SQL to form this HAVING statement
	 *
	 * @param sql The sql string
	 * @return The new HAVING statement
	 */
	default Having havingSQL(final String sql) {
		return having().sql(sql);
	}

}
