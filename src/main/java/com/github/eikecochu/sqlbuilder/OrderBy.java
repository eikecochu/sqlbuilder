package com.github.eikecochu.sqlbuilder;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import lombok.experimental.Accessors;

@ToString
@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class OrderBy extends SQLQueryPart<OrderBy> implements QueryBuilder, BeforeUnion {

	@ToString
	@Getter(AccessLevel.PROTECTED)
	private static class OrderByTerm implements QueryPart {

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

	private BeforeOrderBy builder;
	private final List<OrderByTerm> orderByTerms = new ArrayList<>();

	protected OrderBy(final BeforeOrderBy builder) {
		this.builder = builder;
	}

	/**
	 * Order by a column name and ascending or descending
	 *
	 * @param name      The column name to order by
	 * @param ascending true for ascending order, false for descending order
	 * @return This ORDER BY statement
	 */
	public OrderBy column(final String name, final boolean ascending) {
		orderByTerms.add(new OrderByTerm(name, ascending));
		return this;
	}

	/**
	 * Add a column to order by. Column name can also contain order direction, e.g.
	 * "COL DESC"
	 *
	 * @param name The column name to order by, including optional order direction
	 * @return This ORDER BY statement
	 */
	public OrderBy column(String name) {
		if (name != null) {
			final String nameUpper = name.toUpperCase()
					.trim();
			boolean ascending = true;
			if (nameUpper.endsWith(" DESC")) {
				ascending = false;
				name = name.replaceAll("\\s+[Dd][Ee][Ss][Cc]\\s*$", "");
			} else
				name = name.replaceAll("\\s+[Aa][Ss][Cc]\\s*$", "");
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

	/**
	 * Order by multiple columns
	 *
	 * @param names The column names to order by
	 * @return This ORDER BY statement
	 */
	public OrderBy columns(final String... names) {
		if (names != null)
			for (final String name : names)
				column(name);
		return this;
	}

	/**
	 * Order ascending by a column
	 *
	 * @param name The column name to order by in ascending order
	 * @return This ORDER BY statement
	 */
	public OrderBy asc(final String name) {
		return column(name, true);
	}

	/**
	 * Order descending by a column
	 *
	 * @param name The column name to order by in descending order
	 * @return This ORDER BY statement
	 */
	public OrderBy desc(final String name) {
		return column(name, false);
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (builder != null)
			strings.add(builder.string(options));

		if (sql() != null)
			strings.add(sql());
		else {
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
