package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class GroupBy implements QueryBuilder, QueryPart, Orderable {

	private final Where where;
	private final List<String> columns = new ArrayList<>();

	protected GroupBy(Where where) {
		this.where = where;
	}

	public GroupBy(Where where, String... columns) {
		this(where);
		columns(columns);
	}

	public GroupBy column(String column) {
		columns.add(column);
		return this;
	}

	public GroupBy columns(String... columns) {
		if (columns != null)
			for (String column : columns)
				this.columns.add(column);
		return this;
	}

	public Having having() {
		return new Having(this);
	}

	@Override
	public OrderBy orderBy() {
		return new OrderBy(this);
	}

	@Override
	public OrderBy orderBy(String... columns) {
		return new OrderBy(this, columns);
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

		strings.add(where.string(options));

		if (!columns.isEmpty()) {
			strings.add(options.newLine());
			strings.add(options.pad("GROUP BY"));

			StringJoiner columnsStrings = new StringJoiner();
			for (String column : columns)
				columnsStrings.add(QueryUtils.splitName(column)
					.string(options));
			strings.add(" ");
			strings.add(columnsStrings.toString(", "));
		}

		return strings.toString();
	}

}
