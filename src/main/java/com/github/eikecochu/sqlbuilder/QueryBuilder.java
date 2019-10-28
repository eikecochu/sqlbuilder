package com.github.eikecochu.sqlbuilder;

public interface QueryBuilder extends QueryPart {

	/**
	 * Returns the Query representation of this builder instance
	 * @return The Query representation
	 */
	default Query query() {
		return new Query(this);
	}

}
