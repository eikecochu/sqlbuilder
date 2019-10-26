package de.ec.sql.before;

import de.ec.sql.Delete;
import de.ec.sql.Table;

public interface BeforeDelete {

	Delete delete(String table);

	Delete delete(Table table);

	Delete delete(Delete delete);

}
