package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Exists extends SQLQueryPart<Exists> implements QueryBuilder {

	private QueryBuilder builder;

	Exists(final QueryBuilder builder) {
		this.builder = builder;
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (sql() != null) {
			strings.add(sql());
		} else if (builder != null) {
			strings.add(options.padCased("EXISTS ("));

			final QueryOptions subOptions = options.copy()
					.indentLevel(options.indentLevel() + 1);
			strings.add(subOptions.newLine(true));
			strings.add(builder.string(subOptions)
					.trim());

			strings.add(")");
		}

		return strings.toString();
	}

}
