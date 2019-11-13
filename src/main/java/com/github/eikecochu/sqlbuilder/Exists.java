package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * The EXISTS expression. This expression is used to create a constraint with a
 * subquery to check if some elements exist.
 */
@ToString
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Exists extends QueryPartImpl<Exists> implements QueryBuilder<Exists> {

	private final QueryBuilder<?> query;

	protected Exists(final QueryBuilder<?> query) {
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

			options.indent();
			strings.add(options.newLine(true));
			strings.add(query.string(options)
					.trim());
			options.unindent();

			strings.add(")");
		}

		return strings.toString();
	}

}
