package de.ec.sql;

public interface BeforeUpdate {

	Update update(String table);

	Update update(Table table);

	Update update(Update update);

}
