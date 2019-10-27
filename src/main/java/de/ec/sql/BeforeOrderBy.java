package de.ec.sql;

public interface BeforeOrderBy {

	OrderBy orderBy();

	OrderBy orderBy(String... columns);

	OrderBy orderBy(OrderBy orderBy);

}
