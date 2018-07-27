package de.ec.sql;

public class Condition extends Conditionable<Condition> {

	@Override
	public String string() {
		return string(QueryOptions.DEFAULT_OPTIONS);
	}

	@Override
	public String string(QueryOptions options) {
		return "(" + super.string(options) + ")";
	}

}
