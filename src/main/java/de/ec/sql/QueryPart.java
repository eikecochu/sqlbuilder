package de.ec.sql;

public interface QueryPart {

	default String string() {
		return string(QueryOptions.DEFAULT_OPTIONS);
	}

	String string(QueryOptions options);

}
