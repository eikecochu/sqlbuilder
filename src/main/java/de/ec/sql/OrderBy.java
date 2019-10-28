package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class OrderBy implements QueryBuilder {

	private BeforeOrderBy builder;
	private final List<OrderByTerm> orderByTerms = new ArrayList<>();
	private String sql;

	protected OrderBy(final BeforeOrderBy builder) {
		this.builder = builder;
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
	public String string() {
		return string(QueryOptions.DEFAULT_OPTIONS);
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (builder != null)
			strings.add(builder.string(options));

		if (sql != null) {
			strings.add(sql);
		} else {
			final StringJoiner orderStrings = new StringJoiner();
			for (final OrderByTerm orderByTerm : orderByTerms)
				orderStrings.add(orderByTerm.string(options));

			if (!orderStrings.isEmpty()) {
				strings.add(options.newLine());
				strings.add(options.padCased("ORDER BY"));
				strings.add(" ");
				strings.add(orderStrings.toString(", "));
			}
		}

		return strings.toString();
	}

}
