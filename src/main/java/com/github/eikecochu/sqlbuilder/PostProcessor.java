package com.github.eikecochu.sqlbuilder;

import java.sql.Connection;

public interface PostProcessor<T> {

	/**
	 * Transforms the passed value into another value
	 *
	 * @param value      The input value
	 * @param connection The connection used by the Query builder
	 * @return The output value
	 */
	T process(T value, QueryOptions options, Connection connection);

}
