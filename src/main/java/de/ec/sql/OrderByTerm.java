package de.ec.sql;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
class OrderByTerm implements QueryPart {

	private final Name name;
	private final boolean ascending;

	protected OrderByTerm(final String name, final boolean ascending) {
		this(null, name, ascending);
	}

	protected OrderByTerm(final String schema, final String name, final boolean ascending) {
		this.name = new Name().schema(schema)
				.name(name);
		this.ascending = ascending;
	}

	@Override
	public String string() {
		return string(QueryOptions.DEFAULT_OPTIONS);
	}

	@Override
	public String string(final QueryOptions options) {
		return name.string(options) + " " + options.cased(ascending ? "ASC" : "DESC");
	}

}
