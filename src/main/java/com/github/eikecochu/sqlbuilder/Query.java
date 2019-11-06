package com.github.eikecochu.sqlbuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter(AccessLevel.PROTECTED)
public class Query extends QueryPartImpl<Query> implements QueryBuilder {

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	@Accessors(fluent = true)
	private QueryOptions options;

	Query(final QueryPart parent) {
		super(parent);
	}

	@Override
	public String toString() {
		return string();
	}

	@Override
	public String string(final QueryOptions options) {
		return string(options, null);
	}

	/**
	 * Transforms this statement into an SQL string
	 *
	 * @param options    The QueryOptions to apply for transformation
	 * @param connection Optional database connection
	 * @return The created SQL string
	 */
	public String string(final QueryOptions options, final Connection connection) {
		String sql = sql();
		if (sql == null && parent() != null)
			sql = parent().string(safeOptions(options));

		if (options.sqlPostprocessor() != null)
			sql = options.sqlPostprocessor()
					.process(sql, options.copy(), connection);

		return sql;
	}

	/**
	 * Prepares the statement using the passed database connection
	 *
	 * @param connection The database connection
	 * @return The PreparedStatement
	 * @throws SQLException if preparing fails
	 */
	public PreparedStatement prepare(final Connection connection) throws SQLException {
		return prepare(connection, options);
	}

	/**
	 * Prepares the statement using the passed database connection
	 *
	 * @param connection The database connection
	 * @param options    The QueryOptions to use
	 * @return The PreparedStatement
	 * @throws SQLException if preparing fails
	 */
	public PreparedStatement prepare(final Connection connection, QueryOptions options) throws SQLException {
		options = safeOptions(options).prepare(true);

		final String sql = string(options, connection);

		PreparedStatement stmt;
		if (options.returnGeneratedKeys())
			stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		else
			stmt = connection.prepareStatement(sql);

		stmt = options.applyStatementOptions(stmt);

		if (options.stmtPostprocessor() != null)
			stmt = options.stmtPostprocessor()
					.process(stmt, options.copy(), connection);

		int index = 1;
		for (Object value : options.preparedValues()) {
			if (value != null && options.valueConverters() != null && options.valueConverters()
					.containsKey(value.getClass()))
				value = options.valueConverters()
						.get(value.getClass())
						.apply(value);
			stmt.setObject(index++, value);
		}

		return stmt;
	}

	private QueryOptions safeOptions(QueryOptions options) {
		options = options == null ? QueryOptions.DEFAULT_OPTIONS : options;
		return options.copy()
				.query(this);
	}

	@Override
	public Query query() {
		return this;
	}

}
