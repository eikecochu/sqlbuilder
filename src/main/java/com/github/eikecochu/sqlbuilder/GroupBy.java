package com.github.eikecochu.sqlbuilder;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * The GROUP BY expression. Allows the grouping of selected data under some
 * columns to apply aggregate functions on it.
 */
@ToString
@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class GroupBy extends QueryPartImpl<GroupBy>
		implements QueryBuilder<GroupBy>, BeforeOrderBy<GroupBy>, BeforeHaving<GroupBy>, BeforeUnion<GroupBy> {

	private final List<String> columns = new ArrayList<>();

	protected GroupBy(final BeforeGroupBy<?> parent) {
		super(parent);
	}

	/**
	 * Group by column name
	 *
	 * @param column The column name to group by
	 * @return This GROUP BY statement
	 */
	public GroupBy column(final String column) {
		columns.add(column);
		return this;
	}

	/**
	 * Group by column names
	 *
	 * @param columns The column names to group by
	 * @return This GROUP BY statement
	 */
	public GroupBy columns(final String... columns) {
		if (columns != null)
			this.columns.addAll(StringUtils.filterValues(columns));
		return this;
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (parent() != null)
			strings.add(parent().string(options));

		if (strings.notEmpty())
			strings.add(options.newLine());

		if (sql() != null)
			strings.add(options.padded(sql()));
		else if (!columns.isEmpty()) {
			strings.add(options.padCased("GROUP BY"));

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
