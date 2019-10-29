package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Delete extends SQLQueryPart<Delete> implements QueryBuilder, BeforeWhere {

	private BeforeDelete builder;
	private final String table;

	/**
	 * Create a new DELETE statement
	 *
	 * @param table The name of the table to delete from
	 */
	public Delete(final String table) {
		this.table = table;
	}

	/**
	 * Create a new DELETE statement
	 *
	 * @param table The Table representation of the table to delete from
	 */
	public Delete(final Table table) {
		this(table.tableName());
	}

	protected Delete(final BeforeDelete builder, final String table) {
		this(table);
		this.builder = builder;
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (builder != null) {
			strings.add(builder.string(options));
			strings.add(options.newLine());
		}

		if (sql() != null)
			strings.add(sql());
		else {
			strings.add(options.padCased("DELETE FROM"));

			strings.add(" ");
			strings.add(table);
		}

		return strings.toString();
	}

}
