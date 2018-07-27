package de.ec.sql;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class Having extends Conditionable<Having> implements QueryBuilder, QueryPart {

	private final GroupBy groupBy;
	private Conditionable<Having> condition;

	protected Having(GroupBy groupBy) {
		this.groupBy = groupBy;
	}

	public OrderBy orderBy() {
		return new OrderBy(this);
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

		strings.add(groupBy.string(options));

		if (condition != null) {
			strings.add(options.newLine());
			strings.add(options.pad("HAVING"));
			strings.add(" ");
			strings.add(condition.string(options));
		}

		return strings.toString();
	}

}
