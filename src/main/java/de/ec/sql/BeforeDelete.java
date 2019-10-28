package de.ec.sql;

public interface BeforeDelete extends QueryPart {

	default Delete delete(String table) {
		return new Delete(this, table);
	}

	default Delete delete(Table table) {
		return delete(table.tableName());
	}

	default Delete delete(Delete delete) {
		return delete.builder(this);
	}

}
