package de.ec.sql.before;

import de.ec.sql.Select;

public interface BeforeSelect {

	Select select();

	Select select(String... columns);

	Select select(Select select);

}
