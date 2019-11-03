package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(fluent = true)
public abstract class QueryPartImpl<T extends QueryPartImpl<T>> implements QueryPart {

	@Getter(AccessLevel.PROTECTED)
	private QueryPart parent;

	@Getter(AccessLevel.PROTECTED)
	private String sql;

	public QueryPartImpl(final QueryPart parent) {
		this.parent = parent;
	}

	/**
	 * Set SQL as content
	 * 
	 * @param sql The SQL expression
	 * @return This instance
	 */
	public T sql(final String sql) {
		this.sql = sql;
		return self();
	}

	/**
	 * Set the parent of this expression
	 * 
	 * @param parent The parent expression
	 * @return This instance
	 */
	public T parent(final QueryPart parent) {
		this.parent = parent;
		return self();
	}

	/**
	 * Continue the building chain with any element, for example custom implemented
	 * expressions.
	 * 
	 * @param ext The custom expression
	 * @return The custom expression
	 */
	public <U extends QueryPartImpl<U>> U ext(final U ext) {
		if (ext == null)
			return null;
		return ext.parent(this);
	}

	@SuppressWarnings("unchecked")
	private T self() {
		return (T) this;
	}

}
