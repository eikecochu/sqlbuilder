package de.ec.sql;

public interface BeforeWith extends QueryPart {

	default With with(final String name) {
		return new With(this, name);
	}

	default With with(final With with) {
		return with.builder(this);
	}

	default With withSQL(final String sql) {
		return with((String) null).sql(sql);
	}

}
