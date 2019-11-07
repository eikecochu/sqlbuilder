package com.github.eikecochu.sqlbuilder;

public interface QueryPartSQL<T extends QueryPartSQL<T>> extends QueryPart {

	/**
	 * Set SQL as content
	 *
	 * @param sql The SQL expression
	 * @return This instance
	 */
	T sql(final String sql);

}
