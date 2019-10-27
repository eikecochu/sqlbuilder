package de.ec.sql;

public interface BeforeSelect {

	Select select();

	Select select(String... columns);

	Select select(Select select);

}
