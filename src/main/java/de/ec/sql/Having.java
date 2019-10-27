package de.ec.sql;

import de.ec.sql.Keyword.SecondaryKeyword;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter(AccessLevel.PROTECTED)
public class Having extends Conditionable<Having> implements QueryBuilder, BeforeOrderBy, SecondaryKeyword {

	@Setter(AccessLevel.PACKAGE)
	private GroupBy groupBy;
	private Conditionable<Having> condition;

	protected Having(final GroupBy groupBy) {
		this.groupBy = groupBy;
	}

	@Override
	public OrderBy orderBy() {
		return new OrderBy(this);
	}

	@Override
	public OrderBy orderBy(final String... columns) {
		return new OrderBy(this).columns(columns);
	}

	@Override
	public OrderBy orderBy(final OrderBy orderBy) {
		orderBy.setHaving(this);
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

		if (groupBy != null)
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
