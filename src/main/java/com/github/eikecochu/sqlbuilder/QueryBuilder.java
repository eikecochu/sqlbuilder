package com.github.eikecochu.sqlbuilder;

public interface QueryBuilder<T extends QueryBuilder<T>> extends QueryPartLinked<T> {

	/**
	 * Returns the Query representation of this builder instance
	 *
	 * @return The Query representation
	 */
	default Query query() {
		return new Query(this);
	}

	/**
	 * Allows the user to finish the query building process by passing the query to
	 * a specified processor.
	 *
	 * @param <U>       The generic type
	 * @param processor The query processor
	 * @return The query processor
	 */
	default <U extends QueryProcessor> U query(final U processor) {
		if (processor != null)
			processor.process(query());
		return processor;
	}

}
