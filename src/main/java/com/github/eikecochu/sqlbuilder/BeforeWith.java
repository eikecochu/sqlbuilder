package com.github.eikecochu.sqlbuilder;

/**
 * Implemented by keywords that precede the WITH statement.
 */
public interface BeforeWith extends QueryPart {

	/**
	 * Continue query with WITH
	 *
	 * @param name The name of the with-block
	 * @return The new WITH statement
	 */
	default With with(final String name) {
		return new With(this, name);
	}

	/**
	 * Accept an existing WITH statement as predecessor
	 *
	 * @param with The existing WITH statement
	 * @return Returns the passed WITH statement
	 */
	default With with(final With with) {
		return with.parent(this);
	}

	/**
	 * Use plain SQL to form this WITH statement
	 *
	 * @param sql The sql string
	 * @return The new WITH statement
	 */
	default With withSQL(final String sql) {
		return with((String) null).sql(sql);
	}

}
