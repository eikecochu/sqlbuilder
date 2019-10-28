package de.ec.sql;

public interface BeforeDelete extends QueryPart {

	default Delete delete(final String table) {
		return new Delete(this, table);
	}

	default Delete delete(final Table table) {
		return delete(table.tableName());
	}

	default Delete delete(final Delete delete) {
		return delete.builder(this);
	}

	default Delete deleteSQL(final String sql) {
		return delete((String) null).sql(sql);
	}

}
