package de.ec.sql;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
class OrderByTerm implements QueryPart {

	private final Name name;
	private final boolean ascending;

	protected OrderByTerm(String name, boolean ascending) {
		this(null, name, ascending);
	}

	protected OrderByTerm(String schema, String name, boolean ascending) {
		this.name = new Name().schema(schema)
			.name(name);
		this.ascending = ascending;
	}

	@Override
	public String string() {
		return string(QueryOptions.DEFAULT_OPTIONS);
	}

	@Override
	public String string(QueryOptions options) {
		return name.string(options) + " " + options.cased(ascending ? "ASC" : "DESC");
	}

}
