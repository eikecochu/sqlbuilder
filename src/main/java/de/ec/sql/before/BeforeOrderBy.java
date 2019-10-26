package de.ec.sql.before;

import de.ec.sql.OrderBy;

public interface BeforeOrderBy {

	OrderBy orderBy();

	OrderBy orderBy(String... columns);

	OrderBy orderBy(OrderBy orderBy);

}
