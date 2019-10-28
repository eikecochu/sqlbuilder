package com.github.eikecochu.sqlbuilder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class QueryOptions {

	public static int FETCH_ALL = 0;

	static QueryOptions DEFAULT_OPTIONS = new QueryOptions();

	private int padLength = "SELECT".length();
	private boolean splitNames = true;
	private boolean pretty = true;
	private boolean indent = true;
	private boolean uppercase = true;
	private boolean quote = false;
	private char quoteStartChar = '"';
	private char quoteEndChar = '"';
	private String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private PostProcessor<String> sqlPostprocessor = null;
	private PostProcessor<PreparedStatement> stmtPostprocessor = null;
	private Map<Class<?>, Function<Object, Object>> valueConverters = null;
	private boolean returnGeneratedKeys = false;
	private int fetchSize = FETCH_ALL;
	private int fetchDirection = ResultSet.FETCH_FORWARD;
	private boolean escapeKeywords = false;

	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	private int indentLevel = 0;

	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	private boolean prepare;

	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.NONE)
	private final List<Object> preparedValues = new ArrayList<>();

	private Query query;

	void addPreparedValue(final Object value) {
		preparedValues.add(value);
	}

	String indentString() {
		if (pretty && indent) {
			return StringUtils.repeat(' ', indentLevel * padLength + (indentLevel == 0 ? 0 : 1));
		} else
			return "";
	}

	String newLine() {
		return pretty ? "\n" + indentString() : " ";
	}

	String padCased(final String keyword) {
		if (pretty && keyword != null) {
			final int length = Math.max(0, padLength - keyword.split("\\s+")[0].length());
			return StringUtils.leftPad("", length) + cased(keyword);
		}
		return cased(keyword);
	}

	String ticked(final String string) {
		if (string == null)
			return null;
		return quote || (escapeKeywords && Name.isKeyword(string)) ? quoteStartChar + string + quoteEndChar : string;
	}

	String cased(final String string) {
		if (string == null)
			return null;
		return uppercase ? string.toUpperCase() : string.toLowerCase();
	}

	QueryOptions copy() {
		return new QueryOptions().padLength(padLength)
				.splitNames(splitNames)
				.pretty(pretty)
				.indent(indent)
				.uppercase(uppercase)
				.quote(quote)
				.quoteStartChar(quoteStartChar)
				.quoteEndChar(quoteEndChar)
				.dateFormat(dateFormat)
				.sqlPostprocessor(sqlPostprocessor)
				.stmtPostprocessor(stmtPostprocessor)
				.valueConverters(valueConverters == null ? null : new HashMap<>(valueConverters))
				.returnGeneratedKeys(returnGeneratedKeys)
				.fetchSize(fetchSize)
				.fetchDirection(fetchDirection)
				.escapeKeywords(escapeKeywords)
				.indentLevel(indentLevel);
	}

	String preparedValuesString() {
		final StringBuilder sb = new StringBuilder();
		int index = 1;
		String prefix = "";
		for (final Object value : preparedValues) {
			sb.append(prefix)
					.append(index++)
					.append(": ")
					.append(value.toString());
			prefix = ", ";
		}
		return sb.toString();
	}

	PreparedStatement applyStatementOptions(final PreparedStatement stmt) throws SQLException {
		if (fetchSize != FETCH_ALL) {
			stmt.setFetchSize(fetchSize);
			stmt.setMaxRows(fetchSize);
		}
		stmt.setFetchDirection(fetchDirection);
		return stmt;
	}

	/**
	 * Fetch all columns on query
	 * @return This QueryOptions instance
	 */
	public QueryOptions fetchAll() {
		return fetchSize(FETCH_ALL);
	}

	/**
	 * Fetch the first row only on query
	 * @return This QueryOptions instance
	 */
	public QueryOptions fetchFirst() {
		return fetchSize(1);
	}

	/**
	 * Register a converter for a specific class to be used for value conversion
	 * @param clazz The class of the values to be converted
	 * @param func The value converter
	 * @return This QueryOptions instance
	 */
	@SuppressWarnings("unchecked")
	public <T> QueryOptions convert(final Class<T> clazz, final Function<T, Object> func) {
		if (valueConverters == null)
			valueConverters = new HashMap<>();
		valueConverters.put(clazz, (Function<Object, Object>) func);
		return this;
	}

	/**
	 * Returns the default options
	 * @return The default options
	 */
	public static QueryOptions getDefaultOptions() {
		return DEFAULT_OPTIONS.copy();
	}

	/**
	 * Sets the default options
	 * @param options The new default options
	 */
	public static void setDefaultOptions(final QueryOptions options) {
		DEFAULT_OPTIONS = options.copy();
	}

}
