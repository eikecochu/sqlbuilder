package com.github.eikecochu.sqlbuilder;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * The Query class is the final class of the building process and describes a
 * complete query.
 */
@Getter(AccessLevel.PROTECTED)
public class Query extends QueryPartImpl<Query> implements QueryBuilder<Query> {

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	@Accessors(fluent = true)
	private QueryOptions options;

	Query(final QueryPartLinked<?> parent) {
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
					.process(sql, safeOptions(options), connection);

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
		if (isExpression())
			throw new RuntimeException("use prepareCall() to prepare function calls");
		options = safeOptions(options).prepare(true);

		final String sql = string(options, connection).trim();

		PreparedStatement stmt;
		if (options.returnGeneratedKeys())
			stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		else
			stmt = connection.prepareStatement(sql);

		stmt = options.applyStatementOptions(stmt);

		if (options.stmtPostprocessor() != null)
			stmt = options.stmtPostprocessor()
					.process(stmt, safeOptions(options), connection);

		int index = 1;
		for (final Object value : options.preparedValues())
			stmt.setObject(index++, value);

		return stmt;
	}

	/**
	 * Prepares the call using the passed database connection
	 *
	 * @param connection The database connection
	 * @param options    The QueryOptions to use
	 * @return The CallableStatement
	 * @throws SQLException if preparing fails
	 */
	public CallableStatement prepareCall(final Connection connection, QueryOptions options) throws SQLException {
		if (!isExpression())
			throw new RuntimeException("use prepare() to prepare query statements");
		options = safeOptions(options).prepare(true);

		final String sql = string(options, connection).trim();

		CallableStatement stmt = connection.prepareCall(sql);

		stmt = options.applyStatementOptions(stmt);

		if (options.callPostprocessor() != null)
			stmt = options.callPostprocessor()
					.process(stmt, safeOptions(options), connection);

		int index = 1;

		// register return parameter
		final Expression expr = (Expression) parent();
		if (expr.returnType() != 0)
			stmt.registerOutParameter(index++, expr.returnType());

		// insert values
		for (final Object value : options.preparedValues())
			stmt.setObject(index++, value);

		// register OUT parameters
		int paramOffset = expr.returnType() == 0 ? 1 : 2;
		for (final int type : expr.paramTypes()) {
			if (type != 0)
				stmt.registerOutParameter(paramOffset, type);
			paramOffset++;
		}

		return stmt;
	}

	private QueryOptions safeOptions(QueryOptions options) {
		options = options == null ? QueryOptions.getDefaultOptions() : options;
		return options.query(this);
	}

	/**
	 * Returns the statement type. The type is determined by the first expression of
	 * the builder, that is not a WITH expression.
	 *
	 * @return The statement type as Class
	 */
	public Class<?> statementType() {
		QueryPartLinked<?> part = this;
		while (part != null && part.parent() != null && !(part.parent() instanceof With))
			part = part.parent();
		if (part == null)
			return null;
		return part.getClass();
	}

	/**
	 * Checks if this builder represents the passed statement type
	 *
	 * @param clazz The statement type to check
	 * @return True if types match
	 */
	public boolean isStatementType(final Class<?> clazz) {
		final Class<?> type = statementType();
		if (clazz == null && type == null)
			return true;
		if (clazz == null ^ type == null)
			return false;
		return clazz.isAssignableFrom(type);
	}

	/**
	 * Checks if this builder represents a SELECT statement
	 *
	 * @return True if SELECT
	 */
	public boolean isSelect() {
		return isStatementType(Select.class);
	}

	/**
	 * Checks if this builder represents an INSERT statement
	 *
	 * @return True if INSERT
	 */
	public boolean isInsert() {
		return isStatementType(Insert.class);
	}

	/**
	 * Checks if this builder represents an UPDATE statement
	 *
	 * @return True if UPDATE
	 */
	public boolean isUpdate() {
		return isStatementType(Update.class);
	}

	/**
	 * Checks if this builder represents a DELETE statement
	 *
	 * @return True if DELETE
	 */
	public boolean isDelete() {
		return isStatementType(Delete.class);
	}

	/**
	 * Checks if this builder represents a function expression
	 *
	 * @return True if expression
	 */
	public boolean isExpression() {
		return isStatementType(Expression.class);
	}

	@Override
	public Query query() {
		return this;
	}

}
