package com.github.eikecochu.sqlbuilder;

import java.sql.Connection;

/**
 * The PostProcessor interface is used to create postprocessing instances for
 * postprocessing multiple elements during the building process of the query.
 *
 * @param <T> The type of the value passed into the postprocessor
 */
public interface PostProcessor<T> {

	/**
	 * Transforms the passed value into another value
	 *
	 * @param value      The input value
	 * @param options    The query options
	 * @param connection The connection used by the Query builder
	 * @return The output value
	 */
	T process(T value, QueryOptions options, Connection connection);

}
