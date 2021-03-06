package com.github.eikecochu.sqlbuilder;

/**
 * Implemented by keywords that precede the SELECT statement.
 *
 * @param <T> the generic type returned by some fluent methods of this interface
 */
public interface BeforeSelect<T extends BeforeSelect<T>> extends QueryPart, QueryPartSQL<T>, QueryPartLinked<T> {

	/**
	 * Continue query with SELECT
	 *
	 * @return The new SELECT statement
	 */
	default Select select() {
		return new Select(this);
	}

	/**
	 * Continue query with SELECT
	 *
	 * @param columns The column names to select
	 * @return The new SELECT statement
	 */
	default Select select(final String... columns) {
		return select().columns(columns);
	}

	/**
	 * Accept an existing SELECT statement as predecessor
	 *
	 * @param select The existing SELECT statement
	 * @return Returns the passed SELECT statement
	 */
	default Select select(final Select select) {
		return select.parent(this);
	}

	/**
	 * Use plain SQL to form this SELECT statement
	 *
	 * @param sql The sql string
	 * @return The new SELECT statement
	 */
	default Select selectSQL(final String sql) {
		return select().sql(sql);
	}

}
