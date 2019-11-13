package com.github.eikecochu.sqlbuilder.mysql;

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
 * Limit statement
 *
 * Supported by MySQL
 */
@ToString
@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Limit extends QueryPartImpl<Limit> implements QueryBuilder<Limit> {

	private int limit = 0;

	/**
	 * Create a new Limit statement with the amount of rows to be fetched
	 * 
	 * @param limit The amount of rows to be fetched
	 */
	public Limit(final int limit) {
		this(null, limit);
	}

	protected Limit(final QueryPartLinked<?> parent, final int limit) {
		this.limit = limit;
	}

	/**
	 * Creates a new Offset instance with the offset to skip when selecting data
	 *
	 * @param offset The offset to skip from the start
	 * @return The new Offset instance
	 */
	public Offset offset(final int offset) {
		return new Offset(this, offset);
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (parent() != null)
			strings.add(parent().string(options));

		if (strings.notEmpty())
			strings.add(options.newLine());

		if (limit > 0) {
			strings.add(options.padCased("LIMIT"));
			strings.add(" " + limit);
		}

		return strings.toString();
	}

}
