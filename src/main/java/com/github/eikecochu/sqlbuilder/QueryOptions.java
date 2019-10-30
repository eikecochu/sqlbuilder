package com.github.eikecochu.sqlbuilder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Getter
@Setter
@Accessors(fluent = true)
public class QueryOptions {

	public static int FETCH_ALL = 0;

	static QueryOptions DEFAULT_OPTIONS = new QueryOptions();

	/**
	 * The default length to pad to. If pretty printing is enabled, keywords on new
	 * lines are left padded with whitespace to align the right side of each keyword
	 * in the statement to this number.
	 *
	 * Default is set to the length of the word "SELECT" = 6.
	 */
	private int padLength = "SELECT".length();

	/**
	 * Enable to try to split names, such as table and column names, by schema and
	 * alias etc to enable better quotation support if required and enabled.
	 */
	private boolean splitNames = true;

	/**
	 * Enable to pretty print outputted strings. Will align keywords right to the
	 * length set in the padLength variable.
	 */
	private boolean pretty = true;

	/**
	 * Enable to indent when pretty printing. Will indent subqueries to the left of
	 * the padded minimum length.
	 */
	private boolean indent = true;

	/**
	 * Enable to convert keywords etc to uppercase.
	 */
	private boolean uppercase = true;

	/**
	 * Enable to wrap keywords etc in what is set in the quoteStartChar and
	 * quoteEndChar. If disabled, conflicting keywords may still be quoted, unless
	 * explicitly disabled via escapeKeywords variable.
	 */
	private boolean quote = false;

	/**
	 * Use as starting quote character. Used when quote is enabled.
	 */
	private char quoteStartChar = '"';

	/**
	 * Use as ending quote character. Used when quote is enabled.
	 */
	private char quoteEndChar = '"';

	/**
	 * The date format used to parse strings.
	 */
	private String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	/**
	 * The SQL postprocessor used when an SQL string is generated, before it is
	 * returned.
	 */
	private PostProcessor<String> sqlPostprocessor = null;

	/**
	 * The Statement postprocessor used when a statement is prepared, before it is
	 * returned.
	 */
	private PostProcessor<PreparedStatement> stmtPostprocessor = null;

	/**
	 * Value converters used to convert values of classes into other values. To add
	 * a value converter for booleans for example, call convert(Boolean.class, b ->
	 * ...) to register the converter. Only one converter per class type is allowed.
	 */
	private Map<Class<?>, Function<Object, Object>> valueConverters = null;

	/**
	 * Fetch option. Will enable returning generated database keys after the
	 * statement is executed.
	 */
	private boolean returnGeneratedKeys = false;

	/**
	 * Fetch option. Will set the maximum number of records fetched from the
	 * database when the statement is executed.
	 */
	private int fetchSize = FETCH_ALL;

	/**
	 * Fetch option. Will set the fetch direction if the fetch size is limited. Set
	 * to any value available in ResultSet.FETCH_FORWARD etc.
	 */
	private int fetchDirection = ResultSet.FETCH_FORWARD;

	/**
	 * Enable to escape keywords regardless of what the quote setting is set to.
	 * Disable with quote to disable all quoting.
	 */
	private boolean escapeKeywords = false;

	/**
	 * The line delimiter used to create pretty printed statements.
	 */
	private String lineDelimiter = "\n";

	/**
	 * Will push each condition of a list of conditions, separated by operators, on
	 * a new line. For example WHERE A = 1(NEWLINE)AND B = 2.
	 */
	private boolean conditionOnNewline = false;

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
		if (pretty && indent)
			return StringUtils.repeat(' ', indentLevel * padLength + (indentLevel == 0 ? 0 : 1));
		else
			return "";
	}

	String newLine() {
		return newLine(false);
	}

	String newLine(final boolean noSpace) {
		return pretty ? lineDelimiter + indentString() : noSpace ? "" : " ";
	}

	String padCased(final String keyword) {
		return cased(padded(keyword));
	}

	String ticked(final String string) {
		if (string == null)
			return null;
		return quote || (escapeKeywords && Name.isKeyword(string)) ? quoteStartChar + string + quoteEndChar : string;
	}

	String padded(final String keyword) {
		if (pretty && keyword != null) {
			final int length = Math.max(0, padLength - keyword.split("\\s+")[0].length());
			return StringUtils.leftPad("", length) + cased(keyword);
		}
		return keyword;
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
				.lineDelimiter(lineDelimiter)
				.conditionOnNewline(conditionOnNewline)
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
	 *
	 * @return This QueryOptions instance
	 */
	public QueryOptions fetchAll() {
		return fetchSize(FETCH_ALL);
	}

	/**
	 * Fetch the first row only on query
	 *
	 * @return This QueryOptions instance
	 */
	public QueryOptions fetchFirst() {
		return fetchSize(1);
	}

	/**
	 * Register a converter for a specific class to be used for value conversion
	 *
	 * @param clazz The class of the values to be converted
	 * @param func  The value converter
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
	 *
	 * @return The default options
	 */
	public static QueryOptions getDefaultOptions() {
		return DEFAULT_OPTIONS.copy();
	}

	/**
	 * Sets the default options
	 *
	 * @param options The new default options
	 */
	public static void setDefaultOptions(final QueryOptions options) {
		DEFAULT_OPTIONS = options.copy();
	}

}
