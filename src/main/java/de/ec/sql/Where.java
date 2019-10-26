package de.ec.sql;

import de.ec.sql.before.BeforeGroupBy;
import de.ec.sql.before.BeforeOrderBy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PROTECTED)
public class Where extends Conditionable<Where> implements QueryBuilder, QueryPart, BeforeGroupBy, BeforeOrderBy {

	@Setter(AccessLevel.PACKAGE)
	private QueryPart builder;

	public Where() {
	}

	protected Where(final Join join) {
		builder = join;
	}

	protected Where(final Delete delete) {
		builder = delete;
	}

	protected Where(final Update update) {
		builder = update;
	}

	@Override
	public GroupBy groupBy() {
		return new GroupBy(this);
	}

	@Override
	public GroupBy groupBy(final String... columns) {
		return new GroupBy(this, columns);
	}

	@Override
	public GroupBy groupBy(final GroupBy groupBy) {
		groupBy.setWhere(this);
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

		if (builder != null)
			strings.add(builder.string(options));

		final String condition = super.string(options);

		if (condition != null && !condition.isEmpty()) {
			strings.add(options.newLine());
			strings.add(options.pad("WHERE"));
			strings.add(" ");
			strings.add(condition);
		}

		return strings.toString();
	}

}
