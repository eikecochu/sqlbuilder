package com.github.eikecochu.sqlbuilder;

/**
 * Implemented by keywords that precede the GROUP BY statement.
 */
public interface BeforeGroupBy extends QueryPart {

	/**
	 * Continue query with GROUP BY
	 * @return The new GROUP BY statement
	 */
	default GroupBy groupBy() {
		return new GroupBy(this);
	}

	/**
	 * Continue query with GROUP BY
	 * @param columns The column names to group by
	 * @return The new GROUP BY statement
	 */
	default GroupBy groupBy(final String... columns) {
		return groupBy().columns(columns);
	}

	/**
	 * Accept an existing GROUP BY statement as predecessor
	 * @param groupBy The existing GROUP BY statement
	 * @return Returns the passed GROUP BY statement
	 */
	default GroupBy groupBy(final GroupBy groupBy) {
		return groupBy.builder(this);
	}

	/**
	 * Use plain SQL to form this GROUP BY statement
	 * @param sql The sql string
	 * @return The new GROUP BY statement
	 */
	default GroupBy groupBySQL(final String sql) {
		return groupBy().sql(sql);
	}

}
