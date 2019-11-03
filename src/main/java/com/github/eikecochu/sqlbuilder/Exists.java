package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Exists extends QueryPartImpl<Exists> implements QueryBuilder {

	private final QueryPart query;

	protected Exists(final QueryBuilder query) {
		super(null);
		this.query = query;
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (parent() != null)
			strings.add(parent().string(options));

		if (sql() != null)
			strings.add(sql());
		else if (query != null) {
			strings.add(options.padCased("EXISTS ("));

			final QueryOptions subOptions = options.copy()
					.indentLevel(options.indentLevel() + 1);
			strings.add(subOptions.newLine(true));
			strings.add(query.string(subOptions)
					.trim());

			strings.add(")");
		}

		return strings.toString();
	}

}
