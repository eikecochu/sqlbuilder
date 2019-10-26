package de.ec.sql.before;

import java.util.ArrayList;
import java.util.List;

import de.ec.sql.From;
import de.ec.sql.Table;

public interface BeforeFrom {

	From from();

	From from(String... tables);

	From from(From from);

	default From from(final Table... tables) {
		final List<String> names = new ArrayList<>(tables.length);
		for (final Table table : tables)
			names.add(table.tableName());
		return from(names.toArray(new String[tables.length]));
	}

}
