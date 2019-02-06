package de.ec.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class Query implements QueryPart, QueryBuilder {

	private static final Logger log = LoggerFactory.getLogger(Query.class);

	private QueryPart builder;
	private PostProcessor processor;

	protected Query(QueryPart builder) {
		this.builder = builder;
	}

	public Query postprocess(PostProcessor processor) {
		this.processor = processor;
		return this;
	}

	@Override
	public String toString() {
		return string();
	}

	@Override
	public String string() {
		return string(QueryOptions.DEFAULT_OPTIONS);
	}

	@Override
	public String string(QueryOptions options) {
		return builder.string(options);
	}

	private PreparedStatement prepareInternal(Connection connection, Integer autoGeneratedKeys) throws SQLException {
		QueryOptions options = new QueryOptions().prepare(true)
			.pretty(false);
		String sql = string(options);
		
		if(processor != null)
			sql = processor.process(sql);

		log.info("Query preparation\n  ┌ Query     : " + sql + "\n  └ Parameters: [" + options.preparedValuesString()
				+ "]");

		PreparedStatement stmt;
		if (autoGeneratedKeys == null)
			stmt = connection.prepareStatement(sql);
		else
			stmt = connection.prepareStatement(sql, autoGeneratedKeys);

		int index = 1;
		for (Object value : options.preparedValues())
			stmt.setObject(index++, value);

		return stmt;
	}

	public PreparedStatement prepare(Connection connection) throws SQLException {
		return prepareInternal(connection, null);
	}

	public PreparedStatement prepare(Connection connection, int autoGeneratedKeys) throws SQLException {
		return prepareInternal(connection, autoGeneratedKeys);
	}

	public String preparedString() {
		return preparedString(new QueryOptions());
	}

	public String preparedString(QueryOptions options) {
		options.prepare(true);
		return string(options);
	}

	@Override
	public Query query() {
		return this;
	}

}
