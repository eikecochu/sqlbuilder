package de.ec.sql;

import de.ec.sql.Join.JoinMode;

public interface BeforeJoin extends QueryPart {

	default Join join(final JoinMode mode) {
		return new Join(this, mode);
	}

	default Join join() {
		return join(JoinMode.INNER_JOIN);
	}

	default Join join(final String table) {
		return join().table(table);
	}

	default Join join(final Join join) {
		return join.builder(this);
	}

	default Join join(final Table table) {
		return join(table.tableName());
	}

	default Join joinSQL(final String sql) {
		return new Join(this, null).sql(sql);
	}

	default Join innerJoin() {
		return join();
	}

	default Join innerJoin(final String table) {
		return join().table(table);
	}

	default Join innerJoin(final Table table) {
		return innerJoin(table.tableName());
	}

	default Join crossJoin() {
		return join(JoinMode.CROSS_JOIN);
	}

	default Join crossJoin(final String table) {
		return crossJoin().table(table);
	}

	default Join crossJoin(final Table table) {
		return crossJoin(table.tableName());
	}

	default Join outerJoin() {
		return join(JoinMode.OUTER_JOIN);
	}

	default Join outerJoin(final String table) {
		return outerJoin().table(table);
	}

	default Join outerJoin(final Table table) {
		return outerJoin(table.tableName());
	}

	default Join fullOuterJoin() {
		return outerJoin();
	}

	default Join fullOuterJoin(final String table) {
		return outerJoin(table);
	}

	default Join fullOuterJoin(final Table table) {
		return fullOuterJoin(table.tableName());
	}

	default Join leftJoin() {
		return join(JoinMode.LEFT_JOIN);
	}

	default Join leftJoin(final String table) {
		return leftJoin().table(table);
	}

	default Join leftJoin(final Table table) {
		return leftJoin(table.tableName());
	}

	default Join leftOuterJoin() {
		return leftJoin();
	}

	default Join leftOuterJoin(final String table) {
		return leftJoin(table);
	}

	default Join leftOuterJoin(final Table table) {
		return leftOuterJoin(table.tableName());
	}

	default Join rightJoin() {
		return join(JoinMode.RIGHT_JOIN);
	}

	default Join rightJoin(final String table) {
		return rightJoin().table(table);
	}

	default Join rightJoin(final Table table) {
		return rightJoin(table.tableName());
	}

	default Join rightOuterJoin() {
		return rightJoin();
	}

	default Join rightOuterJoin(final String table) {
		return rightJoin(table);
	}

	default Join rightOuterJoin(final Table table) {
		return rightOuterJoin(table.tableName());
	}

}
