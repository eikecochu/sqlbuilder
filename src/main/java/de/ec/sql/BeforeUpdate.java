package de.ec.sql;

public interface BeforeUpdate extends QueryPart {

	default Update update(String table) {
		return new Update(this, table);
	}

	default Update update(Table table) {
		return update(table.tableName());
	}

	default Update update(Update update) {
		return update.builder(this);
	}

}
