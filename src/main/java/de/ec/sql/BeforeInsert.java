package de.ec.sql;

public interface BeforeInsert {

	Insert insert(String table);

	Insert insert(Table table);

	Insert insert(Insert insert);

}
