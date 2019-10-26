package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PROTECTED)
public class OrderBy implements QueryBuilder, QueryPart {

	@Setter(AccessLevel.PACKAGE)
	private Having having;
	private final List<OrderByTerm> orderByTerms = new ArrayList<>();

	public OrderBy() {
	}

	protected OrderBy(final GroupBy groupBy) {
		having = new Having(groupBy);
	}

	protected OrderBy(final GroupBy groupBy, final String... names) {
		this(groupBy);
		columns(names);
	}

	protected OrderBy(final Having having) {
		this.having = having;
	}

	public OrderBy column(final String name, final boolean ascending) {
		orderByTerms.add(new OrderByTerm(name, ascending));
		return this;
	}

	public OrderBy column(String name) {
		if (name != null) {
			final String nameUpper = name.toUpperCase()
					.trim();
			boolean ascending = true;
			if (nameUpper.endsWith(" DESC")) {
				ascending = false;
				name = name.replaceAll("\\s+[Dd][Ee][Ss][Cc]\\s*$", "");
			} else {
				name = name.replaceAll("\\s+[Aa][Ss][Cc]\\s*$", "");
			}
			String schema = null;
			if (name.contains(".")) {
				final String[] parts = name.split("\\.");
				schema = parts[0];
				name = parts[1];
			}
			orderByTerms.add(new OrderByTerm(schema, name, ascending));
		}
		return this;
	}

	public OrderBy columns(final String... names) {
		if (names != null)
			for (final String name : names)
				column(name);
		return this;
	}

	public OrderBy asc(final String name) {
		return column(name, true);
	}

	public OrderBy desc(final String name) {
		return column(name, false);
	}

	@Override
	public Query query() {
		return new Query(this);
	}

	@Override
	public String string() {
		return string(QueryOptions.DEFAULT_OPTIONS);
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (having != null)
			strings.add(having.string(options));

		final StringJoiner orderStrings = new StringJoiner();
		for (final OrderByTerm orderByTerm : orderByTerms)
			orderStrings.add(orderByTerm.string(options));

		if (!orderStrings.isEmpty()) {
			strings.add(options.newLine());
			strings.add(options.pad("ORDER BY"));
			strings.add(" ");
			strings.add(orderStrings.toString(", "));
		}

		return strings.toString();
	}

}
