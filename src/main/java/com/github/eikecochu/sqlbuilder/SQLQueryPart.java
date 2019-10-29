package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

public abstract class SQLQueryPart<T extends SQLQueryPart<T>> implements QueryPart {

	@Getter(AccessLevel.PROTECTED)
	@Accessors(fluent = true)
	private String sql;

	@SuppressWarnings("unchecked")
	public T sql(final String sql) {
		this.sql = sql;
		return (T) this;
	}

}
