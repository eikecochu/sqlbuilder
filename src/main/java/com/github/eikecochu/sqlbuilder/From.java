package com.github.eikecochu.sqlbuilder;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class From extends SQLQueryPart<From>
		implements QueryBuilder, BeforeJoin, BeforeWhere, BeforeGroupBy, BeforeOrderBy, BeforeUnion {

	@Data
	@Accessors(fluent = true)
	private class FromOrigin implements QueryPart {

		private String table;
		private Query subquery;
		private String alias;

		@Override
		public String string(final QueryOptions options) {
			if (table != null)
				return QueryUtils.splitName(options, table)
						.string(options) + (alias != null ? " " + alias : "");
			return "(" + subquery.string(options) + ")" + (alias != null ? " " + alias : "");
		}

	}

	private BeforeFrom builder;
	private final List<FromOrigin> origins = new ArrayList<>();

	protected From(final BeforeFrom builder) {
		this.builder = builder;
	}

	/**
	 * Specify the target table by name
	 *
	 * @param table The table name to select from
	 * @return This FROM statement
	 */
	public From table(String table) {
		String alias = null;
		if (table.toUpperCase()
				.contains(" AS "))
			table = table.replaceAll("\\s+[Aa][Ss]\\s+", " ");
		final String[] parts = table.split("\\s+");
		if (parts.length > 1) {
			table = parts[0];
			alias = parts[1];
		}
		origins.add(new FromOrigin().table(table)
				.alias(alias));
		return this;
	}

	/**
	 * Specify the target table by Table representation
	 *
	 * @param table The Table representation to select from
	 * @return This FROM statement
	 */
	public From table(final Table table) {
		return table(table.tableName());
	}

	/**
	 * Specify the target tables by name
	 *
	 * @param table The table names to select from
	 * @return This FROM statement
	 */
	public From tables(final String... tables) {
		for (final String table : tables)
			table(table);
		return this;
	}

	/**
	 * Specify the target table by Table representations
	 *
	 * @param table The Table representations to select from
	 * @return This FROM statement
	 */
	public From tables(final Table... tables) {
		for (final Table table : tables)
			table(table);
		return this;
	}

	/**
	 * Specify a subquery to select from
	 *
	 * @param query The subquery to select from
	 * @param alias The subquery alias
	 * @return This FROM statement
	 */
	public From subquery(final Query query, final String alias) {
		origins.add(new FromOrigin().subquery(query)
				.alias(alias));
		return this;
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (builder != null) {
			strings.add(builder.string(options));
			strings.add(options.newLine());
		}

		if (sql() != null)
			strings.add(sql());
		else {
			strings.add(options.padCased("FROM"));
			strings.add(" ");

			final StringJoiner fromStrings = new StringJoiner();
			for (final FromOrigin origin : origins)
				fromStrings.add(origin.string(options));

			strings.add(fromStrings.toString(", "));
		}

		return strings.toString();
	}

}
