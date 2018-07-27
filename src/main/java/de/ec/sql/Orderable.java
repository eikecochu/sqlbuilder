package de.ec.sql;

public interface Orderable {

	OrderBy orderBy();

	OrderBy orderBy(String... columns);

}
