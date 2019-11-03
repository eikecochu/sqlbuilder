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
public class Select extends QueryPartImpl<Select> implements BeforeFrom {

	private final List<String> columns = new ArrayList<>();
	private boolean distinct = false;
	private boolean all = false;

	/**
	 * Create a new SELECT statement
	 *
	 * @param columns The column names to select
	 */
	public Select(final String... columns) {
		columns(columns);
	}

	protected Select(final BeforeSelect parent) {
		super(parent);
	}

	/**
	 * Enable DISTINCT selection. Disables ALL selection.
	 *
	 * @return This SELECT statement
	 */
	public Select distinct() {
		distinct = true;
		all = false;
		return this;
	}

	/**
	 * Enable ALL selection. Disables DISTINCT selection.
	 *
	 * @return This SELECT statement
	 */
	public Select all() {
		distinct = false;
		all = true;
		return this;
	}

	/**
	 * Select a column by name
	 *
	 * @param column The column name of the column to select
	 * @return This SELECT statement
	 */
	public Select column(final String column) {
		columns.add(column);
		return this;
	}

	/**
	 * Select multiple columns by name
	 *
	 * @param columns The column names of the columns to select
	 * @return This SELECT statement
	 */
	public Select columns(final String... columns) {
		if (columns != null)
			this.columns.addAll(Arrays.asList(columns));
		return this;
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (parent() != null) {
			strings.add(parent().string(options));
			strings.add(options.newLine());
		}

		if (sql() != null)
			strings.add(sql());
		else {
			strings.add(options.padCased("SELECT"));

			if (distinct) {
				strings.add(" ");
				strings.add(options.cased("DISTINCT"));
			} else if (all) {
				strings.add(" ");
				strings.add(options.cased("ALL"));
			}

			if (columns.isEmpty())
				strings.add(" *");
			else {
				final StringJoiner columnsStrings = new StringJoiner();
				for (final String column : columns)
					columnsStrings.add(QueryUtils.splitName(options, column)
							.string(options));
				strings.add(" ");
				strings.add(columnsStrings.toString(", "));
			}
		}

		return strings.toString();
	}

}
