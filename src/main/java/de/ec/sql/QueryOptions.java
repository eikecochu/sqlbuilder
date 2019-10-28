package de.ec.sql;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
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
	private boolean backticks = false;
	private char quoteStartChar = '"';
	private char quoteEndChar = '"';
	private String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private PostProcessor<String> sqlPostprocessor = null;
	private PostProcessor<PreparedStatement> stmtPostprocessor = null;
	private Function<Object, Object> valueConverter = null;
	private boolean returnGeneratedKeys = false;
	private int fetchSize = FETCH_ALL;
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
		return backticks || (escapeKeywords && Name.isKeyword(string)) ? quoteStartChar + string + quoteEndChar
				: string;
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
				.backticks(backticks)
				.quoteStartChar(quoteStartChar)
				.quoteEndChar(quoteEndChar)
				.dateFormat(dateFormat)
				.sqlPostprocessor(sqlPostprocessor)
				.stmtPostprocessor(stmtPostprocessor)
				.valueConverter(valueConverter)
				.returnGeneratedKeys(returnGeneratedKeys)
				.fetchSize(fetchSize)
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

	public QueryOptions fetchAll() {
		return fetchSize(FETCH_ALL);
	}

	public QueryOptions fetchFirst() {
		return fetchSize(1);
	}

	public static QueryOptions getDefaultOptions() {
		return DEFAULT_OPTIONS.copy();
	}

	public static void setDefaultOptions(final QueryOptions options) {
		DEFAULT_OPTIONS = options.copy();
	}

}
