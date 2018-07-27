package de.ec.sql;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class Where extends Conditionable<Where> implements QueryBuilder, QueryPart, Groupable, Orderable {

	private final QueryPart builder;

	protected Where(Join join) {
		builder = join;
	}

	protected Where(Delete delete) {
		builder = delete;
	}

	protected Where(Update update) {
		builder = update;
	}

	@Override
	public GroupBy groupBy() {
		return new GroupBy(this);
	}

	@Override
	public GroupBy groupBy(String... columns) {
		return new GroupBy(this, columns);
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

		strings.add(builder.string(options));

		String condition = super.string(options);

		if (condition != null && !condition.isEmpty()) {
			strings.add(options.newLine());
			strings.add(options.pad("WHERE"));
			strings.add(" ");
			strings.add(condition);
		}

		return strings.toString();
	}

}
