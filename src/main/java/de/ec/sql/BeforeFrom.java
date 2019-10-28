package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

public interface BeforeFrom extends QueryPart {

	default From from() {
		return new From(this);
	}

	default From from(String... tables) {
		return new From(this).tables(tables);
	}

	default From from(final Table... tables) {
		final List<String> names = new ArrayList<>(tables.length);
		for (final Table table : tables)
			names.add(table.tableName());
		return from(names.toArray(new String[tables.length]));
	}

	default From from(From from) {
		return from.builder(this);
	}

}
