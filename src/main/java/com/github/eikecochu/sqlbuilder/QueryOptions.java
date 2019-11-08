package com.github.eikecochu.sqlbuilder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Getter
@Setter
@Accessors(fluent = true)
public class QueryOptions {

	public static int FETCH_ALL = 0;

	@NonNull
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

	/**
	 * Enable to remove conditions with null values. For example if A = ? AND B = 1
	 * with A = null, then the condition evaluates to B = 1. Disable to treat null
	 * values as if the IS NULL operator was used, which will evaluate to A IS NULL
	 * AND B = 1.
	 */
	private boolean ignoreNull = true;

	/**
	 * The default placeholder. If an expression is evaluated and parameters are
	 * inserted, this placeholder is used to replace null values. In databases like
	 * MSSQL, default parameters are invoked by passing "default". In Oracle, the
	 * parameter is just left out. Leave to null or "" to remove null parameters
	 * from parameter lists. If disabled, default parameters must be at the end of
	 * the parameter list.
	 */
	private String defaultPlaceholder = null;

	/**
	 * Enable to treat unrecognizable names as simple names. If disabled,
	 * unrecognizable names will throw a RuntimeException.
	 */
	private boolean ignoreUnrecognizableNames = true;

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

	public String indentString() {
		if (pretty && indent)
			return StringUtils.repeat(' ', indentLevel * padLength + (indentLevel == 0 ? 0 : 1));
		else
			return "";
	}

	public String newLine() {
		return newLine(false);
	}

	public String newLine(final boolean noSpace) {
		return pretty ? lineDelimiter + indentString() : noSpace ? "" : " ";
	}

	public String padCased(final String keyword) {
		return cased(padded(keyword));
	}

	public String ticked(final String string) {
		if (string == null)
			return null;
		return quote || (escapeKeywords && Name.isKeyword(string)) ? quoteStartChar + string + quoteEndChar : string;
	}

	public String padded(final String keyword) {
		if (pretty && keyword != null) {
			final int length = Math.max(0, padLength - keyword.split("\\s+")[0].length());
			return StringUtils.leftPad("", length) + cased(keyword);
		}
		return keyword;
	}

	public String cased(final String string) {
		if (string == null)
			return null;
		return uppercase ? string.toUpperCase() : string.toLowerCase();
	}

	public QueryOptions copy() {
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
				.returnGeneratedKeys(returnGeneratedKeys)
				.fetchSize(fetchSize)
				.fetchDirection(fetchDirection)
				.escapeKeywords(escapeKeywords)
				.lineDelimiter(lineDelimiter)
				.conditionOnNewline(conditionOnNewline)
				.ignoreNull(ignoreNull)
				.defaultPlaceholder(defaultPlaceholder)
				.ignoreUnrecognizableNames(ignoreUnrecognizableNames)
				.indentLevel(indentLevel);
	}

	public String preparedValuesString() {
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
		stmt.setFetchSize(fetchSize);
		stmt.setMaxRows(fetchSize);
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
		if (options != null)
			DEFAULT_OPTIONS = options.copy();
	}

}
