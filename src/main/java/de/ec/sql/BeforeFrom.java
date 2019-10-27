package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

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
