package de.ec.sql;

public interface BeforeUpdate extends QueryPart {

	default Update update(final String table) {
		return new Update(this, table);
	}

	default Update update(final Table table) {
		return update(table.tableName());
	}

	default Update update(final Update update) {
		return update.builder(this);
	}

	default Update updateSQL(final String sql) {
		return update((String) null).sql(sql);
	}

}
