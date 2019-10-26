package de.ec.sql.before;

import de.ec.sql.GroupBy;

public interface BeforeGroupBy {

	GroupBy groupBy();

	GroupBy groupBy(String... columns);

	GroupBy groupBy(GroupBy groupBy);

}
