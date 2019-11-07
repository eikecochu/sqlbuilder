package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(fluent = true)
public abstract class QueryPartImpl<T extends QueryPartImpl<T>>
		implements QueryPart, QueryPartSQL<T>, QueryPartLinked<T> {

	@Getter
	private QueryPartLinked<?> parent;

	@Getter(AccessLevel.PROTECTED)
	private String sql;

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
