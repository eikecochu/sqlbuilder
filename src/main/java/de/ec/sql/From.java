package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import de.ec.sql.Keyword.SecondaryKeyword;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class From implements QueryBuilder, BeforeJoin, BeforeWhere, BeforeGroupBy, BeforeOrderBy, SecondaryKeyword {

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

	public From table(final Table table) {
		return table(table.tableName());
	}

	public From tables(final String... tables) {
		for (final String table : tables)
			table(table);
		return this;
	}

	public From tables(final Table... tables) {
		for (final Table table : tables)
			table(table);
		return this;
	}

	public From subquery(final Query query, final String alias) {
		origins.add(new FromOrigin().subquery(query)
				.alias(alias));
		return this;
	}

	@Override
	public String string(final QueryOptions options) {
		assert !origins.isEmpty() : "from statement must have at least one target";

		final StringJoiner strings = new StringJoiner();

		if (builder != null) {
			strings.add(builder.string(options));
			strings.add(options.newLine());
		}

		strings.add(options.padCased("FROM"));
		strings.add(" ");

		final StringJoiner fromStrings = new StringJoiner();
		for (final FromOrigin origin : origins)
			fromStrings.add(origin.string(options));

		strings.add(fromStrings.toString(", "));

		return strings.toString();
	}

}
