package de.ec.sql;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class Join extends Conditionable<Join>
		implements QueryBuilder, QueryPart, Joinable, Whereable, Groupable, Orderable {

	public static enum JoinMode implements QueryPart {
		INNER_JOIN("INNER JOIN"),
		OUTER_JOIN("OUTER JOIN"),
		LEFT_JOIN("LEFT JOIN"),
		RIGHT_JOIN("RIGHT JOIN"),
		CROSS_JOIN("CROSS JOIN");

		private final String string;

		private JoinMode(String string) {
			this.string = string;
		}

		@Override
		public String string() {
			return string(QueryOptions.DEFAULT_OPTIONS);
		}

		@Override
		public String string(QueryOptions options) {
			return options.cased(string);
		}
	}

	private final From from;
	private final Join join;
	private final JoinMode joinMode;
	private Query query;
	private String name;

	protected Join(From from) {
		this(from, JoinMode.INNER_JOIN);
	}

	protected Join(From from, JoinMode joinMode) {
		this.from = from;
		join = null;
		this.joinMode = joinMode;
	}

	private Join(Join join, JoinMode joinMode) {
		from = null;
		this.join = join;
		this.joinMode = joinMode;
	}

	public Join subquery(Query query, String name) {
		this.query = query;
		this.name = name;
		return this;
	}

	public Join table(String name) {
		query = null;
		this.name = name;
		return this;
	}

	public Conditionable<Join> on() {
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
		return new Where(this);
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
		StringJoiner strings = new StringJoiner();

		if (join != null)
			strings.add(join.string(options));
		else
			strings.add(from.string(options));

		if (name != null) {
			strings.add(options.newLine());

			strings.add(options.pad(joinMode.string(options)));

			if (query != null) {
				strings.add(" ");
				strings.add(query.string(options));
			}

			strings.add(" ");
			strings.add(QueryUtils.splitName(options, name)
				.string(options));

			String condition = super.string(options);

			if (condition != null) {
				strings.add(" ");
				strings.add(options.cased("ON"));
				strings.add(" ");
				strings.add(condition);
			}
		}

		return strings.toString();
	}

}
