package com.github.eikecochu.sqlbuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class GroupBy extends SQLQueryPart<GroupBy> implements QueryBuilder, BeforeOrderBy, BeforeHaving, BeforeUnion {

	private BeforeGroupBy builder;
	private final List<String> columns = new ArrayList<>();

	protected GroupBy(final BeforeGroupBy builder) {
		this.builder = builder;
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
			this.columns.addAll(Arrays.asList(columns));
		return this;
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (builder != null)
			strings.add(builder.string(options));

		if (sql() != null)
			strings.add(sql());
		else if (!columns.isEmpty()) {
			strings.add(options.newLine());
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
