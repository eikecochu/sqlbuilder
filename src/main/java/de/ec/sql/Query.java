package de.ec.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter(AccessLevel.PROTECTED)
public class Query implements QueryBuilder {

	private static final Logger log = LoggerFactory.getLogger(Query.class);

	@Getter(AccessLevel.PUBLIC)
	@Accessors(fluent = true)
	private QueryOptions options;
	private final QueryPart builder;

	Query(final QueryPart builder) {
		this.builder = builder;
	}

	@Override
	public String toString() {
		return string();
	}

	@Override
	public String string(final QueryOptions options) {
		return builder.string(safeOptions(options));
	}

	public Query options(final QueryOptions options) {
		if (options != null)
			options.query(this);
		this.options = options;
		return this;
	}

	public PreparedStatement prepare(final Connection connection) throws SQLException {
		return prepare(connection, safeOptions(options));
	}

	public PreparedStatement prepare(final Connection connection, QueryOptions options) throws SQLException {
		options = safeOptions(options).prepare(true);

		String sql = string(options);

		if (options.sqlPostprocessor() != null)
			sql = options.sqlPostprocessor()
					.process(sql, connection);

		log.info("Query preparation\n  ┌ Query     : " + sql + "\n  └ Parameters: [" + options.preparedValuesString()
				+ "]");

		PreparedStatement stmt;
		if (options.returnGeneratedKeys())
			stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		else
			stmt = connection.prepareStatement(sql);

		stmt.setFetchSize(options.fetchSize());

		if (options.stmtPostprocessor() != null)
			stmt = options.stmtPostprocessor()
					.process(stmt, connection);

		int index = 1;
		for (Object value : options.preparedValues()) {
			if (options.valueConverter() != null)
				value = options.valueConverter()
						.apply(value);
			stmt.setObject(index++, value);
		}

		return stmt;
	}

	public String preparedString() {
		return preparedString(safeOptions(options));
	}

	public String preparedString(final QueryOptions options) {
		return string(safeOptions(options));
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
