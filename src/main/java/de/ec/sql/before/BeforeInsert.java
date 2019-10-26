package de.ec.sql.before;

import de.ec.sql.Insert;
import de.ec.sql.Table;

public interface BeforeInsert {

	Insert insert(String table);

	Insert insert(Table table);

	Insert insert(Insert insert);

}
