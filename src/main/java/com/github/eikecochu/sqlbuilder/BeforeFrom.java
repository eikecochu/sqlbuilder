package com.github.eikecochu.sqlbuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Implemented by keywords that precede the FROM statement.
 *
 * @param <T> the generic type returned by some fluent methods of this interface
 */
public interface BeforeFrom<T extends BeforeFrom<T>> extends QueryPart, QueryPartSQL<T>, QueryPartLinked<T> {

	/**
	 * Continue query with FROM
	 *
	 * @return The new FROM statement
	 */
	default From from() {
		return new From(this);
	}

	/**
	 * Continue query with FROM
	 *
	 * @param tables The names of the tables to select from
	 * @return The new FROM statement
	 */
	default From from(final String... tables) {
		return new From(this).tables(tables);
	}

	/**
	 * Continue query with FROM
	 *
	 * @param tables The Table representations of the tables to select from
	 * @return The new FROM statement
	 */
	default From from(final Table... tables) {
		final List<String> names = new ArrayList<>(tables.length);
		for (final Table table : tables)
			names.add(table.tableName());
		return from(names.toArray(new String[tables.length]));
	}

	/**
	 * Specify a subquery to select from
	 *
	 * @param subquery The subquery to select from
	 * @param alias    The subquery alias
	 * @return This FROM statement
	 */
	default From from(final QueryBuilder<?> subquery, final String alias) {
		return new From(this).subquery(subquery, alias);
	}

	/**
	 * Specify a subquery to select from
	 *
	 * @param subquery The subquery to select from
	 * @return This FROM statement
	 */
	default From from(final QueryBuilder<?> subquery) {
		return new From(this).subquery(subquery);
	}

	/**
	 * Accept an existing FROM statement as predecessor
	 *
	 * @param from The existing FROM statement
	 * @return Returns the passed FROM statement
	 */
	default From from(final From from) {
		return from.parent(this);
	}

	/**
	 * Use plain SQL to form this FROM statement
	 *
	 * @param sql The sql string
	 * @return The new FROM statement
	 */
	default From fromSQL(final String sql) {
		return from().sql(sql);
	}

}
