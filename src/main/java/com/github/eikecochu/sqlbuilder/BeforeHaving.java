package com.github.eikecochu.sqlbuilder;

/**
 * Implemented by keywords that precede the HAVING statement.
 */
public interface BeforeHaving extends QueryPart {

	/**
	 * Continue query with HAVING
	 * @return The new HAVING statement
	 */
	default Having having() {
		return new Having(this);
	}

	/**
	 * Accept an existing HAVING statement as predecessor
	 * @param having The existing HAVING statement
	 * @return Returns the passed HAVING statement
	 */
	default Having having(final Having having) {
		return having.builder(this);
	}

	/**
	 * Use plain SQL to form this HAVING statement
	 * @param sql The sql string
	 * @return The new HAVING statement
	 */
	default Having havingSQL(final String sql) {
		return having().sql(sql);
	}

}
