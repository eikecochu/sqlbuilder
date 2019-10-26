package de.ec.sql.before;

import de.ec.sql.With;

public interface BeforeWith {

	With with(String name);

	With with(With with);

}
