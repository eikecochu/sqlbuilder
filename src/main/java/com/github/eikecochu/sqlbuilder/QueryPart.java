package com.github.eikecochu.sqlbuilder;

/**
 * The QueryPart interface is implemented by all parts of the query building
 * process to turn them into strings for query assembly when building.
 */
public interface QueryPart {

	/**
	 * Transforms this statement into an SQL string using the default QueryOptions
	 *
	 * @return The created SQL string
	 */
	default String string() {
		return string(QueryOptions.getDefaultOptions());
	}

	/**
	 * Transforms this statement into an SQL string
	 *
	 * @param options The QueryOptions to apply for transformation
	 * @return The created SQL string
	 */
	String string(QueryOptions options);

}
