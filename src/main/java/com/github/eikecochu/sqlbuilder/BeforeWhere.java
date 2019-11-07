package com.github.eikecochu.sqlbuilder;

/**
 * Implemented by keywords that precede the WHERE statement.
 */
public interface BeforeWhere<T extends BeforeWhere<T>> extends QueryPart, QueryPartSQL<T>, QueryPartLinked<T> {

	/**
	 * Continue query with WHERE
	 *
	 * @return The new WHERE statement
	 */
	default Where where() {
		return new Where(this);
	}

	/**
	 * Continue query with WHERE
	 *
	 * @param values The ValueHolder to use as WHERE conditions
	 * @return The new WHERE statement
	 */
	default Where where(final ValueHolder values) {
		return where().values(values);
	}

	/**
	 * Accept an existing WHERE statement as predecessor
	 *
	 * @param where The existing WHERE statement
	 * @return Returns the passed WHERE statement
	 */
	default Where where(final Where where) {
		return where.parent(this);
	}

	/**
	 * Use plain SQL to form this WHERE statement
	 *
	 * @param sql The sql string
	 * @return The new WHERE statement
	 */
	default Where whereSQL(final String sql) {
		return where().sql(sql);
	}

}
