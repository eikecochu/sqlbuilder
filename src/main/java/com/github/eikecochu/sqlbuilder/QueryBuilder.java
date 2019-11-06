package com.github.eikecochu.sqlbuilder;

public interface QueryBuilder extends QueryPart {

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
	 * @param processor The query processor
	 * @return The query processor
	 */
	default <T extends QueryProcessor> T query(final T processor) {
		if (processor != null)
			processor.process(query());
		return processor;
	}

}
