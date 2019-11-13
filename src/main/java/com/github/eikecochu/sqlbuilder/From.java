package com.github.eikecochu.sqlbuilder;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * The FROM expression. This expression defines from where the data is selected.
 * Can be a table name or subquery.
 */
@ToString
@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class From extends QueryPartImpl<From> implements QueryBuilder<From>, BeforeJoin<From>, BeforeWhere<From>,
		BeforeGroupBy<From>, BeforeOrderBy<From>, BeforeUnion<From> {

	@ToString
	@Getter
	@Setter
	@Accessors(fluent = true)
	private static class FromOrigin implements QueryPart {

		private String table;
		private QueryBuilder<?> subquery;
		private String alias;

		@Override
		public String string(final QueryOptions options) {
			if (table != null)
				return QueryUtils.splitName(options, table)
						.string(options) + (alias != null ? " " + alias : "");
			return "(" + subquery.string(options) + ")" + (alias != null ? " " + alias : "");
		}
	}

	private final List<FromOrigin> origins = new ArrayList<>();

	protected From(final BeforeFrom<?> parent) {
		super(parent);
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
	 * @param tables The table names to select from
	 * @return This FROM statement
	 */
	public From tables(final String... tables) {
		if (tables != null)
			for (final String table : StringUtils.filterValues(tables))
				table(table);
		return this;
	}

	/**
	 * Specify the target table by Table representations
	 *
	 * @param tables The Table representations to select from
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
	 * @param subquery The subquery to select from
	 * @param alias    The subquery alias
	 * @return This FROM statement
	 */
	public From subquery(final QueryBuilder<?> subquery, final String alias) {
		origins.add(new FromOrigin().subquery(subquery)
				.alias(alias));
		return this;
	}

	/**
	 * Specify a subquery to select from
	 *
	 * @param subquery The subquery to select from
	 * @return This FROM statement
	 */
	public From subquery(final QueryBuilder<?> subquery) {
		return subquery(subquery, null);
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (parent() != null)
			strings.add(parent().string(options));

		if (strings.notEmpty())
			strings.add(options.newLine());

		if (sql() != null)
			strings.add(options.padded(sql()));
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
