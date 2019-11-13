package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * The default QueryPart implementation used by most expressions. Also
 * implements the QueryPart, QueryPartSQL and QueryPartLinked interfaces for
 * convenience.
 *
 * @param <T> The type to return to for various methods
 */
@NoArgsConstructor
@Accessors(fluent = true)
public abstract class QueryPartImpl<T extends QueryPartImpl<T>>
		implements QueryPart, QueryPartSQL<T>, QueryPartLinked<T> {

	@Getter
	private QueryPartLinked<?> parent;

	@Getter(AccessLevel.PROTECTED)
	private String sql;

	/**
	 * Create a new instance with a parent expression
	 *
	 * @param parent The parent expression
	 */
	public QueryPartImpl(final QueryPartLinked<?> parent) {
		this.parent = parent;
	}

	@Override
	public T sql(final String sql) {
		this.sql = sql;
		return self();
	}

	@Override
	public T parent(final QueryPartLinked<?> parent) {
		this.parent = parent;
		return self();
	}

	@SuppressWarnings("unchecked")
	private T self() {
		return (T) this;
	}

}
