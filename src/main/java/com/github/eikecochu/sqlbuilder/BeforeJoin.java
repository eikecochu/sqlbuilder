package com.github.eikecochu.sqlbuilder;

/**
 * Implemented by keywords that precede the JOIN statement.
 */
public interface BeforeJoin<T extends BeforeJoin<T>> extends QueryPart, QueryPartSQL<T>, QueryPartLinked<T> {

	/**
	 * Continue query with the passed JOIN mode
	 *
	 * @param mode The JOIN mode to use
	 * @return The new JOIN statement
	 */
	default Join join(final JoinMode mode) {
		return new Join(this, mode);
	}

	/**
	 * Continue query with INNER JOIN
	 *
	 * @return The new INNER JOIN statement
	 */
	default Join join() {
		return join(JoinMode.INNER_JOIN);
	}

	/**
	 * Continue query with JOIN
	 *
	 * @param table The name of the table to join to
	 * @return The new JOIN statement
	 */
	default Join join(final String table) {
		return join().table(table);
	}

	/**
	 * Continue query with JOIN
	 *
	 * @param table The Table representation of the table to join to
	 * @return The new JOIN statement
	 */
	default Join join(final Table table) {
		return join(table.tableName());
	}

	/**
	 * Accept an existing JOIN statement as predecessor
	 *
	 * @param join The existing JOIN statement
	 * @return Returns the passed JOIN statement
	 */
	default Join join(final Join join) {
		return join.parent(this);
	}

	/**
	 * Use plain SQL to form this JOIN statement
	 *
	 * @param sql The sql string
	 * @return The new JOIN statement
	 */
	default Join joinSQL(final String sql) {
		return new Join(this, null).sql(sql);
	}

	/**
	 * Continue query with INNER JOIN
	 *
	 * @return The new INNER JOIN statement
	 */
	default Join innerJoin() {
		return join();
	}

	/**
	 * Continue query with INNER JOIN
	 *
	 * @param table The name of the table to join to
	 * @return The new INNER JOIN statement
	 */
	default Join innerJoin(final String table) {
		return join().table(table);
	}

	/**
	 * Continue query with INNER JOIN
	 *
	 * @param table The Table representation of the table to join to
	 * @return The new INNER JOIN statement
	 */
	default Join innerJoin(final Table table) {
		return innerJoin(table.tableName());
	}

	/**
	 * Continue query with INNER JOIN and use two columns as condition with col1 =
	 * col2
	 *
	 * @param table The name of the table to join to
	 * @param col1  The first column
	 * @param col2  The second column, equal to col1
	 * @return The new INNER JOIN statement
	 */
	default Join innerJoinOnColsEq(final String table, final String col1, final String col2) {
		return innerJoin(table).onColsEq(col1, col2);
	}

	/**
	 * Continue query with INNER JOIN and use two columns as condition with col1 =
	 * col2
	 *
	 * @param table The Table representation of the table to join to
	 * @param col1  The first column
	 * @param col2  The second column, equal to col1
	 * @return The new INNER JOIN statement
	 */
	default Join innerJoinOnColsEq(final Table table, final String col1, final String col2) {
		return innerJoin(table).onColsEq(col1, col2);
	}

	/**
	 * Continue query with CROSS JOIN
	 *
	 * @return The new CROSS JOIN statement
	 */
	default Join crossJoin() {
		return join(JoinMode.CROSS_JOIN);
	}

	/**
	 * Continue query with CROSS JOIN
	 *
	 * @param table The name of the table to join to
	 * @return The new CROSS JOIN statement
	 */
	default Join crossJoin(final String table) {
		return crossJoin().table(table);
	}

	/**
	 * Continue query with CROSS JOIN
	 *
	 * @param table The Table representation of the table to join to
	 * @return The new CROSS JOIN statement
	 */
	default Join crossJoin(final Table table) {
		return crossJoin(table.tableName());
	}

	/**
	 * Continue query with OUTER JOIN
	 *
	 * @return The new OUTER JOIN statement
	 */
	default Join outerJoin() {
		return join(JoinMode.OUTER_JOIN);
	}

	/**
	 * Continue query with OUTER JOIN
	 *
	 * @param table The name of the table to join to
	 * @return The new OUTER JOIN statement
	 */
	default Join outerJoin(final String table) {
		return outerJoin().table(table);
	}

	/**
	 * Continue query with OUTER JOIN
	 *
	 * @param table The Table representation of the table to join to
	 * @return The new OUTER JOIN statement
	 */
	default Join outerJoin(final Table table) {
		return outerJoin(table.tableName());
	}

	/**
	 * Continue query with FULL OUTER JOIN
	 *
	 * @return The new FULL OUTER JOIN statement
	 */
	default Join fullOuterJoin() {
		return outerJoin();
	}

	/**
	 * Continue query with FULL OUTER JOIN
	 *
	 * @param table The name of the table to join to
	 * @return The new FULL OUTER JOIN statement
	 */
	default Join fullOuterJoin(final String table) {
		return outerJoin(table);
	}

	/**
	 * Continue query with FULL OUTER JOIN
	 *
	 * @param table The Table representation of the table to join to
	 * @return The new FULL OUTER JOIN statement
	 */
	default Join fullOuterJoin(final Table table) {
		return fullOuterJoin(table.tableName());
	}

	/**
	 * Continue query with LEFT JOIN
	 *
	 * @return The new LEFT JOIN statement
	 */
	default Join leftJoin() {
		return join(JoinMode.LEFT_JOIN);
	}

	/**
	 * Continue query with LEFT JOIN
	 *
	 * @param table The name of the table to join to
	 * @return The new LEFT JOIN statement
	 */
	default Join leftJoin(final String table) {
		return leftJoin().table(table);
	}

	/**
	 * Continue query with LEFT JOIN
	 *
	 * @param table The Table representation of the table to join to
	 * @return The new LEFT JOIN statement
	 */
	default Join leftJoin(final Table table) {
		return leftJoin(table.tableName());
	}

	/**
	 * Continue query with LEFT OUTER JOIN
	 *
	 * @return The new LEFT OUTER JOIN statement
	 */
	default Join leftOuterJoin() {
		return leftJoin();
	}

	/**
	 * Continue query with LEFT OUTER JOIN
	 *
	 * @param table The name of the table to join to
	 * @return The new LEFT OUTER JOIN statement
	 */
	default Join leftOuterJoin(final String table) {
		return leftJoin(table);
	}

	/**
	 * Continue query with LEFT OUTER JOIN
	 *
	 * @param table The Table representation of the table to join to
	 * @return The new LEFT OUTER JOIN statement
	 */
	default Join leftOuterJoin(final Table table) {
		return leftOuterJoin(table.tableName());
	}

	/**
	 * Continue query with RIGHT JOIN
	 *
	 * @return The new RIGHT JOIN statement
	 */
	default Join rightJoin() {
		return join(JoinMode.RIGHT_JOIN);
	}

	/**
	 * Continue query with RIGHT JOIN
	 *
	 * @param table The name of the table to join to
	 * @return The new RIGHT JOIN statement
	 */
	default Join rightJoin(final String table) {
		return rightJoin().table(table);
	}

	/**
	 * Continue query with RIGHT JOIN
	 *
	 * @param table The Table representation of the table to join to
	 * @return The new RIGHT JOIN statement
	 */
	default Join rightJoin(final Table table) {
		return rightJoin(table.tableName());
	}

	/**
	 * Continue query with RIGHT OUTER JOIN
	 *
	 * @return The new RIGHT OUTER JOIN statement
	 */
	default Join rightOuterJoin() {
		return rightJoin();
	}

	/**
	 * Continue query with RIGHT OUTER JOIN
	 *
	 * @param table The name of the table to join to
	 * @return The new RIGHT OUTER JOIN statement
	 */
	default Join rightOuterJoin(final String table) {
		return rightJoin(table);
	}

	/**
	 * Continue query with RIGHT OUTER JOIN
	 *
	 * @param table The Table representation of the table to join to
	 * @return The new RIGHT OUTER JOIN statement
	 */
	default Join rightOuterJoin(final Table table) {
		return rightOuterJoin(table.tableName());
	}

}
