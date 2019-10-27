package de.ec.sql;

public interface BeforeDelete {

	Delete delete(String table);

	Delete delete(Table table);

	Delete delete(Delete delete);

}
