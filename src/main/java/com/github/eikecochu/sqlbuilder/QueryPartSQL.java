package com.github.eikecochu.sqlbuilder;

/**
 * The QueryPartSQL interface is the interface of all expressions that support
 * adding plain SQL into them instead of building methods.
 *
 * @param <T> The type to return to when adding SQL
 */
public interface QueryPartSQL<T extends QueryPartSQL<T>> extends QueryPart {

	/**
	 * Set SQL as content
	 *
	 * @param sql The SQL expression
	 * @return This instance
	 */
	T sql(final String sql);

}
