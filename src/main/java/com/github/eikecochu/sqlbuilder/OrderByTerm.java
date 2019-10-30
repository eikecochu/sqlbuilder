package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter(AccessLevel.PROTECTED)
public class OrderByTerm implements QueryPart {

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
	public String string(final QueryOptions options) {
		return name.string(options) + " " + options.cased(ascending ? "ASC" : "DESC");
	}

}
