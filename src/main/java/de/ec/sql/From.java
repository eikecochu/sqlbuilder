package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import de.ec.sql.Join.JoinMode;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter(AccessLevel.PROTECTED)
public class From implements QueryBuilder, QueryPart, Joinable, Whereable, Groupable, Orderable {

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
		public String string(QueryOptions options) {
			if (table != null)
				return QueryUtils.splitName(table)
					.string(options) + (alias != null ? " " + alias : "");
			return "(" + subquery.string(options) + ")" + (alias != null ? " " + alias : "");
		}

	}

	private final Select select;
	private List<FromOrigin> origins = new ArrayList<>();

	protected From(Select select) {
		this.select = select;
	}

	protected From(Select select, String... tables) {
		this(select);
		tables(tables);
	}

	public From table(String table) {
		String alias = null;
		if (table.toUpperCase()
			.contains(" AS "))
			table = table.replaceAll("\\s+[Aa][Ss]\\s+", " ");
		String[] parts = table.split("\\s+");
		if (parts.length > 1) {
			table = parts[0];
			alias = parts[1];
		}
		origins.add(new FromOrigin().table(table)
			.alias(alias));
		return this;
	}

	public From table(Table table) {
		return table(table.tableName());
	}

	public From tables(String... tables) {
		for (String table : tables)
			table(table);
		return this;
	}

	public From tables(Table...tables) {
		for (Table table : tables)
			table(table);
		return this;
	}

	public From subquery(Query query, String alias) {
		origins.add(new FromOrigin().subquery(query)
			.alias(alias));
		return this;
	}

	@Override
	public Join join() {
		return new Join(this, JoinMode.INNER_JOIN);
	}

	@Override
	public Join join(String table) {
		return join().table(table);
	}

	@Override
	public Join innerJoin() {
		return join();
	}

	@Override
	public Join innerJoin(String table) {
		return innerJoin().table(table);
	}

	@Override
	public Join crossJoin() {
		return new Join(this, JoinMode.CROSS_JOIN);
	}

	@Override
	public Join crossJoin(String table) {
		return crossJoin().table(table);
	}

	@Override
	public Join outerJoin() {
		return new Join(this, JoinMode.OUTER_JOIN);
	}

	@Override
	public Join outerJoin(String table) {
		return outerJoin().table(table);
	}

	@Override
	public Join fullOuterJoin() {
		return outerJoin();
	}

	@Override
	public Join fullOuterJoin(String table) {
		return fullOuterJoin().table(table);
	}

	@Override
	public Join leftJoin() {
		return new Join(this, JoinMode.LEFT_JOIN);
	}

	@Override
	public Join leftJoin(String table) {
		return leftJoin().table(table);
	}

	@Override
	public Join leftOuterJoin() {
		return leftJoin();
	}

	@Override
	public Join leftOuterJoin(String table) {
		return leftOuterJoin().table(table);
	}

	@Override
	public Join rightJoin() {
		return new Join(this, JoinMode.RIGHT_JOIN);
	}

	@Override
	public Join rightJoin(String table) {
		return rightJoin().table(table);
	}

	@Override
	public Join rightOuterJoin() {
		return rightJoin();
	}

	@Override
	public Join rightOuterJoin(String table) {
		return rightOuterJoin().table(table);
	}

	@Override
	public Where where() {
		return new Where(join());
	}
	
	@Override
	public Where where(ValueHolder values) {
		return where().values(values);
	}

	@Override
	public GroupBy groupBy() {
		return new GroupBy(where());
	}

	@Override
	public GroupBy groupBy(String... columns) {
		return new GroupBy(where(), columns);
	}

	@Override
	public OrderBy orderBy() {
		return new OrderBy(groupBy());
	}

	@Override
	public OrderBy orderBy(String... columns) {
		return new OrderBy(groupBy(), columns);
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
	public String string(QueryOptions options) {
		assert !origins.isEmpty() : "from statement must have at least one target";

		StringJoiner strings = new StringJoiner();

		strings.add(select.string(options));
		strings.add(options.newLine());
		strings.add(options.pad("FROM"));
		strings.add(" ");

		StringJoiner fromStrings = new StringJoiner();
		for (FromOrigin origin : origins)
			fromStrings.add(origin.string(options));

		strings.add(fromStrings.toString(", "));

		return strings.toString();
	}

}
