package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import de.ec.sql.Keyword.SecondaryKeyword;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter(AccessLevel.PROTECTED)
public class GroupBy implements QueryBuilder, BeforeOrderBy, BeforeHaving, SecondaryKeyword {

	@Setter(AccessLevel.PACKAGE)
	private Where where;
	private final List<String> columns = new ArrayList<>();

	protected GroupBy(final Where where) {
		this.where = where;
	}

	protected GroupBy(final Where where, final String... columns) {
		this(where);
		columns(columns);
	}

	public GroupBy column(final String column) {
		columns.add(column);
		return this;
	}

	public GroupBy columns(final String... columns) {
		if (columns != null)
			for (final String column : columns)
				this.columns.add(column);
		return this;
	}

	@Override
	public Having having() {
		return new Having(this);
	}

	@Override
	public Having having(final Having having) {
		having.setGroupBy(this);
		return having;
	}

	@Override
	public OrderBy orderBy() {
		return new OrderBy(this);
	}

	@Override
	public OrderBy orderBy(final String... columns) {
		return new OrderBy(this, columns);
	}

	@Override
	public OrderBy orderBy(final OrderBy orderBy) {
		orderBy.setHaving(new Having(this));
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

		if (where != null)
			strings.add(where.string(options));

		if (!columns.isEmpty()) {
			strings.add(options.newLine());
			strings.add(options.pad("GROUP BY"));

			final StringJoiner columnsStrings = new StringJoiner();
			for (final String column : columns)
				columnsStrings.add(QueryUtils.splitName(options, column)
						.string(options));
			strings.add(" ");
			strings.add(columnsStrings.toString(", "));
		}

		return strings.toString();
	}

}
