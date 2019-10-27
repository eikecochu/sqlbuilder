package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import de.ec.sql.Join.JoinMode;
import de.ec.sql.Keyword.SecondaryKeyword;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter(AccessLevel.PROTECTED)
public class From implements QueryBuilder, BeforeJoin, BeforeWhere, BeforeGroupBy, BeforeOrderBy, SecondaryKeyword {

	@Data
	@Accessors(fluent = true)
	private class FromOrigin implements QueryPart {

		private String table;
		private Query subquery;
		private String alias;

		@Override
		public String string() {
			return string(QueryOptions.DEFAULT_OPTIONS);
		}

		@Override
		public String string(final QueryOptions options) {
			if (table != null)
				return QueryUtils.splitName(options, table)
						.string(options) + (alias != null ? " " + alias : "");
			return "(" + subquery.string(options) + ")" + (alias != null ? " " + alias : "");
		}

	}

	@Setter(AccessLevel.PACKAGE)
	private Select select;
	private final List<FromOrigin> origins = new ArrayList<>();

	protected From(final Select select) {
		this.select = select;
	}

	protected From(final Select select, final String... tables) {
		this(select);
		tables(tables);
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
	public Join join() {
		return new Join(this, JoinMode.INNER_JOIN);
	}

	@Override
	public Join join(final String table) {
		return join().table(table);
	}

	@Override
	public Join join(final Join join) {
		join.setFrom(this);
		return join;
	}

	@Override
	public Join innerJoin() {
		return join();
	}

	@Override
	public Join innerJoin(final String table) {
		return innerJoin().table(table);
	}

	@Override
	public Join crossJoin() {
		return new Join(this, JoinMode.CROSS_JOIN);
	}

	@Override
	public Join crossJoin(final String table) {
		return crossJoin().table(table);
	}

	@Override
	public Join outerJoin() {
		return new Join(this, JoinMode.OUTER_JOIN);
	}

	@Override
	public Join outerJoin(final String table) {
		return outerJoin().table(table);
	}

	@Override
	public Join fullOuterJoin() {
		return outerJoin();
	}

	@Override
	public Join fullOuterJoin(final String table) {
		return fullOuterJoin().table(table);
	}

	@Override
	public Join leftJoin() {
		return new Join(this, JoinMode.LEFT_JOIN);
	}

	@Override
	public Join leftJoin(final String table) {
		return leftJoin().table(table);
	}

	@Override
	public Join leftOuterJoin() {
		return leftJoin();
	}

	@Override
	public Join leftOuterJoin(final String table) {
		return leftOuterJoin().table(table);
	}

	@Override
	public Join rightJoin() {
		return new Join(this, JoinMode.RIGHT_JOIN);
	}

	@Override
	public Join rightJoin(final String table) {
		return rightJoin().table(table);
	}

	@Override
	public Join rightOuterJoin() {
		return rightJoin();
	}

	@Override
	public Join rightOuterJoin(final String table) {
		return rightOuterJoin().table(table);
	}

	@Override
	public Where where() {
		return new Where(join());
	}

	@Override
	public Where where(final ValueHolder values) {
		return where().values(values);
	}

	@Override
	public Where where(final Where where) {
		where.setBuilder(this);
		return where;
	}

	@Override
	public GroupBy groupBy() {
		return new GroupBy(where());
	}

	@Override
	public GroupBy groupBy(final String... columns) {
		return new GroupBy(where(), columns);
	}

	@Override
	public GroupBy groupBy(final GroupBy groupBy) {
		groupBy.setWhere(where());
		return groupBy;
	}

	@Override
	public OrderBy orderBy() {
		return new OrderBy(groupBy());
	}

	@Override
	public OrderBy orderBy(final String... columns) {
		return new OrderBy(groupBy(), columns);
	}

	@Override
	public OrderBy orderBy(final OrderBy orderBy) {
		orderBy.setHaving(new Having(groupBy()));
		return orderBy;
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
		assert !origins.isEmpty() : "from statement must have at least one target";

		final StringJoiner strings = new StringJoiner();

		if (select != null)
			strings.add(select.string(options));
		strings.add(options.newLine());
		strings.add(options.pad("FROM"));
		strings.add(" ");

		final StringJoiner fromStrings = new StringJoiner();
		for (final FromOrigin origin : origins)
			fromStrings.add(origin.string(options));

		strings.add(fromStrings.toString(", "));

		return strings.toString();
	}

}
