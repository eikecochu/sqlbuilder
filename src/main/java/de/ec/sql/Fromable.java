package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

public interface Fromable {

	From from();

	From from(String... tables);

	default From from(Table... tables) {
		List<String> names = new ArrayList<>(tables.length);
		for (Table table : tables)
			names.add(table.tableName());
		return from(names.toArray(new String[tables.length]));
	}

}
