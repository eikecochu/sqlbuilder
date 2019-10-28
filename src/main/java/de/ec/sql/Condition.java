package de.ec.sql;

public class Condition extends Conditionable<Condition> implements QueryPart {

	@Override
	public String string(final QueryOptions options) {
		return "(" + super.string(options) + ")";
	}

}
