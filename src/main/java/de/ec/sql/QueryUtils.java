package de.ec.sql;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

abstract class QueryUtils {

	private static final Pattern namePattern = Pattern.compile(
			"^\\s*((?<fn>\\w+)\\()?(`?(?<a1>\\w+)`?\\.)?(`?(?<a2>\\w+)`?\\.)?(`?(?<name>\\w+|\\*)`?)\\)?(\\s+[Aa][Ss])?(\\s+`?(?<alias>\\w+))?`?\\s*$");

	public static Name splitName(QueryOptions options, String strName) {
		if (!options.splitNames())
			return new Name().name(strName);

		Matcher m = namePattern.matcher(strName);
		if (m.matches()) {
			String schema = m.group("a1");
			String table = m.group("a2");

			if (table == null) {
				table = schema;
				schema = null;
			}

			return new Name().function(m.group("fn")).schema(schema).table(table).name(m.group("name"))
					.alias(m.group("alias"));
		} else
			throw new RuntimeException("unrecognizable name: " + strName);
	}

	public static String valueToString(QueryOptions options, Object value) {
		if (value == null)
			return "";
		if (value instanceof String)
			return "'" + QueryUtils.sqlStringEscape(value.toString()) + "'";
		if (value instanceof Integer)
			return Integer.toString((int) value);
		if (value instanceof Long)
			return Long.toString((long) value);
		if (value instanceof Double)
			return Double.toString((double) value);
		if (value instanceof Float)
			return Float.toString((float) value);
		if (value instanceof Boolean)
			return (boolean) value ? "true" : "false";
		if (value instanceof Date)
			return new SimpleDateFormat(options.dateFormat()).format((Date) value);
		return "";
	}

	public static List<String> valuesToStrings(QueryOptions options, Object... values) {
		return Arrays.stream(values).map(value -> valueToString(options, value)).collect(Collectors.toList());
	}

	public static String sqlStringEscape(String string) {
		return string.replaceAll("'(?!')", "''");
	}

	public static String preparedInValues(int count) {
		if (count <= 0)
			return "";
		StringBuilder sb = new StringBuilder();
		sb.append("?");
		count--;
		for (; count > 0; count--)
			sb.append(", ?");
		return sb.toString();
	}

}
