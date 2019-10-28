package de.ec.sql;

public interface BeforeInsert extends QueryPart {

	default Insert insert(final String table) {
		return new Insert(this, table);
	}

	default Insert insert(final Table table) {
		return insert(table.tableName());
	}

	default Insert insert(final Insert insert) {
		return insert.builder(this);
	}

	default Insert insertSQL(final String sql) {
		return insert((String) null).sql(sql);
	}

}
