package de.ec.sql;

public interface QueryBuilder extends QueryPart {

	default Query query() {
		return new Query(this);
	}

}
