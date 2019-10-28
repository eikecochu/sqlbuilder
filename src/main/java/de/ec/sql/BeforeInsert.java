package de.ec.sql;

public interface BeforeInsert extends QueryPart {

	default Insert insert(String table) {
		return new Insert(this, table);
	}

	default Insert insert(Table table) {
		return insert(table.tableName());
	}

	default Insert insert(Insert insert) {
		return insert.builder(this);
	}

}
