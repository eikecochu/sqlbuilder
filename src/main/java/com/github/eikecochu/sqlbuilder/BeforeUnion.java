package com.github.eikecochu.sqlbuilder;

/**
 * Implemented by keywords that precede the UNION statement.
 *
 * @param <T> the generic type returned by some fluent methods of this interface
 */
public interface BeforeUnion<T extends BeforeUnion<T>> extends QueryPart, QueryPartSQL<T>, QueryPartLinked<T> {

	/**
	 * Continue query with UNION
	 *
	 * @return The new UNION statement
	 */
	default Union union() {
		return new Union(this);
	}

	/**
	 * Continue query with UNION ALL
	 *
	 * @return The new UNION ALL statement
	 */
	default Union unionAll() {
		return union().all(true);
	}

	/**
	 * Accept an existing UNION statement as predecessor
	 *
	 * @param union The existing UNION statement
	 * @return Returns the passed UNION statement
	 */
	default Union union(final Union union) {
		return union.parent(this);
	}

	/**
	 * Use plain SQL to form this UNION statement
	 *
	 * @param sql The sql string
	 * @return The new UNION statement
	 */
	default Union unionSQL(final String sql) {
		return union().sql(sql);
	}

}
