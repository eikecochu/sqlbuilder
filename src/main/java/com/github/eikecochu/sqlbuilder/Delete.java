package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Delete extends QueryPartImpl<Delete> implements QueryBuilder, BeforeWhere {

	private final String table;

	/**
	 * Create a new DELETE statement
	 *
	 * @param table The name of the table to delete from
	 */
	public Delete(final String table) {
		this(null, table);
	}

	/**
	 * Create a new DELETE statement
	 *
	 * @param table The Table representation of the table to delete from
	 */
	public Delete(final Table table) {
		this(table.tableName());
	}

	protected Delete(final BeforeDelete parent, final String table) {
		super(parent);
		this.table = table;
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
			strings.add(options.padCased("DELETE FROM"));

			strings.add(" ");
			strings.add(table);
		}

		return strings.toString();
	}

}
