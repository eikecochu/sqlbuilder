package com.github.eikecochu.sqlbuilder.mssql;

import com.github.eikecochu.sqlbuilder.QueryBuilder;
import com.github.eikecochu.sqlbuilder.QueryOptions;
import com.github.eikecochu.sqlbuilder.QueryPartImpl;
import com.github.eikecochu.sqlbuilder.QueryPartLinked;
import com.github.eikecochu.sqlbuilder.StringJoiner;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Fetch statement
 *
 * Supported by MS SQL Server, Oracle 12c, PostgreSQL
 */
@ToString
@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Fetch extends QueryPartImpl<Fetch> implements QueryBuilder<Fetch> {

	private int fetch = 1;

	/**
	 * Create a new Fetch statement with the amount of rows to be fetched
	 * 
	 * @param fetch The amount of rows to be fetched
	 */
	public Fetch(final int fetch) {
		this(null, fetch);
	}

	protected Fetch(final QueryPartLinked<?> parent, final int fetch) {
		this.fetch = fetch;
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (parent() != null)
			strings.add(parent().string(options));

		if (strings.notEmpty())
			strings.add(options.newLine());

		if (fetch > 0) {
			strings.add(options.padCased("FETCH"));
			if (parent() != null && parent() instanceof Offset)
				strings.add(" NEXT");
			else
				strings.add(" FIRST");
			if (fetch > 1)
				strings.add(" " + fetch);
			strings.add(" ");
			strings.add(fetch == 1 ? "ROW" : "ROWS");
			strings.add(" ONLY");
		}

		return strings.toString();
	}

}