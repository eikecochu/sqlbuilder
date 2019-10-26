package de.ec.sql.before;

import de.ec.sql.Table;
import de.ec.sql.Update;

public interface BeforeUpdate {

	Update update(String table);

	Update update(Table table);

	Update update(Update update);

}
