package de.ec.sql;

public interface BeforeJoin extends BeforeWhere {

	Join join();

	Join join(String table);

	Join join(Join join);

	default Join join(final Table table) {
		return join(table.tableName());
	}

	Join innerJoin();

	Join innerJoin(String table);

	default Join innerJoin(final Table table) {
		return innerJoin(table.tableName());
	}

	Join crossJoin();

	Join crossJoin(String table);

	default Join crossJoin(final Table table) {
		return crossJoin(table.tableName());
	}

	Join outerJoin();

	Join outerJoin(String table);

	default Join outerJoin(final Table table) {
		return outerJoin(table.tableName());
	}

	Join fullOuterJoin();

	Join fullOuterJoin(String table);

	default Join fullOuterJoin(final Table table) {
		return fullOuterJoin(table.tableName());
	}

	Join leftJoin();

	Join leftJoin(String table);

	default Join leftJoin(final Table table) {
		return leftJoin(table.tableName());
	}

	Join leftOuterJoin();

	Join leftOuterJoin(String table);

	default Join leftOuterJoin(final Table table) {
		return leftOuterJoin(table.tableName());
	}

	Join rightJoin();

	Join rightJoin(String table);

	default Join rightJoin(final Table table) {
		return rightJoin(table.tableName());
	}

	Join rightOuterJoin();

	Join rightOuterJoin(String table);

	default Join rightOuterJoin(final Table table) {
		return rightOuterJoin(table.tableName());
	}

}
