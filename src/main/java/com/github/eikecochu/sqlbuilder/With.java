package com.github.eikecochu.sqlbuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class With extends SQLQueryPart<With>
		implements BeforeWith, BeforeSelect, BeforeUpdate, BeforeDelete, BeforeInsert {

	private BeforeWith builder;
	private final String name;
	private final List<String> columns = new ArrayList<>();
	private QueryBuilder query;
	private boolean recursive;

	/**
	 * Create a new WITH statement
	 *
	 * @param name The name of the with-block
	 */
	public With(final String name) {
		this.name = name;
	}

	protected With(final BeforeWith builder, final String name) {
		this(name);
		this.builder = builder;
	}

	/**
	 * Flag WITH statement as RECURSIVE
	 *
	 * @return This WITH statement
	 */
	public With recursive() {
		recursive = true;
		return this;
	}

	/**
	 * Add a column to this WITH statement
	 *
	 * @param column The column name to add
	 * @return This WITH statement
	 */
	public With column(final String column) {
		columns.add(column);
		return this;
	}

	/**
	 * Add multiple columns to this WITH statement
	 *
	 * @param columns The column names to add
	 * @return This WITH statement
	 */
	public With columns(final String... columns) {
		this.columns.addAll(Arrays.asList(columns));
		return this;
	}

	/**
	 * Use a subquery as the body of this WITH statement
	 *
	 * @param builder The subquery builder to use as body
	 * @return This WITH statement
	 */
	public With as(final QueryBuilder builder) {
		query = builder;
		return this;
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (builder == null) {
			if (sql() == null) {
				strings.add(options.padCased("WITH"));

				if (recursive) {
					strings.add(" ");
					strings.add(options.cased("RECURSIVE"));
				}
			}
		} else {
			strings.add(options.newLine());
			strings.add(builder.string(options));
			strings.add(",");
		}

		if (strings.notEmpty())
			strings.add(" ");

		if (sql() != null)
			strings.add(sql());
		else {
			strings.add(name);

			if (!columns.isEmpty()) {
				strings.add(" (");
				strings.add(StringUtils.join(columns, ", "));
				strings.add(")");
			}

			strings.add(" ");
			strings.add(options.cased("AS"));
			strings.add(" (");

			final QueryOptions subOptions = options.copy()
					.indentLevel(options.indentLevel() + 1);
			strings.add(subOptions.newLine(true));
			strings.add(query.string(subOptions)
					.trim());

			strings.add(")");
		}

		return strings.toString();
	}

}
