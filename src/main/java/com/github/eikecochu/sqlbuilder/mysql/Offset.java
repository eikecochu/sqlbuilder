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
 * Offset statement
 *
 * Supported by MySQL
 */
@ToString
@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Offset extends QueryPartImpl<Offset> implements QueryBuilder<Offset> {

	private int offset = 0;

	/**
	 * Creates a new Offset instance with the offset to skip when selecting data
	 *
	 * @param offset The offset to skip from the start
	 */
	public Offset(final int offset) {
		this(null, offset);
	}

	protected Offset(final QueryPartLinked<?> parent, final int offset) {
		this.offset = offset;
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (parent() != null)
			strings.add(parent().string(options));

		if (strings.notEmpty())
			strings.add(options.newLine());

		if (offset > 0) {
			strings.add(options.padCased("OFFSET"));
			strings.add(" " + offset);
		}

		return strings.toString();
	}

}
