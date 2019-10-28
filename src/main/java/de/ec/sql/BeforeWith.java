package de.ec.sql;

public interface BeforeWith extends QueryPart {

	default With with(String name) {
		return new With(this, name);
	}

	default With with(With with) {
		return with.builder(this);
	}

}
