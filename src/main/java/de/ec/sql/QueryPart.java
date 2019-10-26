package de.ec.sql;

interface QueryPart {

	String string();

	String string(QueryOptions options);

}
