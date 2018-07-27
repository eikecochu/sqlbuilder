package de.ec.sql;

public interface Groupable {

	GroupBy groupBy();

	GroupBy groupBy(String... columns);

}
