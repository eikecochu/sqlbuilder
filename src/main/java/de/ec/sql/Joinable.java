package de.ec.sql;

public interface Joinable extends Whereable {

	Join join();

	Join join(String table);

	default Join join(Table table) {
		return join(table.tableName());
	}

	Join innerJoin();

	Join innerJoin(String table);

	default Join innerJoin(Table table) {
		return innerJoin(table.tableName());
	}

	Join crossJoin();

	Join crossJoin(String table);

	default Join crossJoin(Table table) {
		return crossJoin(table.tableName());
	}

	Join outerJoin();

	Join outerJoin(String table);

	default Join outerJoin(Table table) {
		return outerJoin(table.tableName());
	}

	Join fullOuterJoin();

	Join fullOuterJoin(String table);

	default Join fullOuterJoin(Table table) {
		return fullOuterJoin(table.tableName());
	}

	Join leftJoin();

	Join leftJoin(String table);

	default Join leftJoin(Table table) {
		return leftJoin(table.tableName());
	}

	Join leftOuterJoin();

	Join leftOuterJoin(String table);

	default Join leftOuterJoin(Table table) {
		return leftOuterJoin(table.tableName());
	}

	Join rightJoin();

	Join rightJoin(String table);

	default Join rightJoin(Table table) {
		return rightJoin(table.tableName());
	}

	Join rightOuterJoin();

	Join rightOuterJoin(String table);

	default Join rightOuterJoin(Table table) {
		return rightOuterJoin(table.tableName());
	}

}
