package de.ec.sql;

import de.ec.sql.before.BeforeGroupBy;
import de.ec.sql.before.BeforeJoin;
import de.ec.sql.before.BeforeOrderBy;
import de.ec.sql.before.BeforeWhere;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PROTECTED)
public class Join extends Conditionable<Join>
		implements QueryBuilder, QueryPart, BeforeJoin, BeforeWhere, BeforeGroupBy, BeforeOrderBy {

	public enum JoinMode implements QueryPart {
		INNER_JOIN("INNER JOIN"),
		OUTER_JOIN("OUTER JOIN"),
		LEFT_JOIN("LEFT JOIN"),
		RIGHT_JOIN("RIGHT JOIN"),
		CROSS_JOIN("CROSS JOIN");

		private final String string;

		private JoinMode(final String string) {
			this.string = string;
		}

		@Override
		public String string() {
			return string(QueryOptions.DEFAULT_OPTIONS);
		}

		@Override
		public String string(final QueryOptions options) {
			return options.cased(string);
		}
	}

	@Setter(AccessLevel.PACKAGE)
	private From from;
	@Setter(AccessLevel.PACKAGE)
	private Join join;
	private final JoinMode joinMode;
	private Query query;
	private String name;

	protected Join(final From from) {
		this(from, JoinMode.INNER_JOIN);
	}

	protected Join(final From from, final JoinMode joinMode) {
		this.from = from;
		join = null;
		this.joinMode = joinMode;
	}

	private Join(final Join join, final JoinMode joinMode) {
		from = null;
		this.join = join;
		this.joinMode = joinMode;
	}

	public Join subquery(final Query query, final String name) {
		this.query = query;
		this.name = name;
		return this;
	}

	public Join table(final String name) {
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
	public Join join(final String table) {
		return join().table(table);
	}

	@Override
	public Join join(final Join join) {
		join.setJoin(this);
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
		return new Where(this);
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
		final StringJoiner strings = new StringJoiner();

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

			final String condition = super.string(options);

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
