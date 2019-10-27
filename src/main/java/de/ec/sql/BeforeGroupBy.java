package de.ec.sql;

public interface BeforeGroupBy {

	GroupBy groupBy();

	GroupBy groupBy(String... columns);

	GroupBy groupBy(GroupBy groupBy);

}
