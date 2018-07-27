package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class QueryOptions {

	private static final int PAD_LENGTH = "SELECT".length();

	public static final QueryOptions DEFAULT_OPTIONS = new QueryOptions();

	private boolean pretty = true;
	private boolean indent = true;
	private int indentLevel = 0;
	private boolean uppercase = true;
	private String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private boolean prepare;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.NONE)
	private List<Object> preparedValues = new ArrayList<>();

	protected void addPreparedValue(Object value) {
		preparedValues.add(value);
	}

	public String indentString() {
		if (pretty && indent) {
			return StringUtils.repeat(' ', indentLevel * PAD_LENGTH + (indentLevel == 0 ? 0 : 1));
		} else
			return "";
	}

	public String newLine() {
		return pretty ? "\n" + indentString() : " ";
	}

	public String pad(String keyword) {
		if (pretty && keyword != null) {
			int length = Math.max(0, PAD_LENGTH - keyword.split("\\s+")[0].length());
			return StringUtils.leftPad("", length) + cased(keyword);
		}
		return cased(keyword);
	}

	public String cased(String string) {
		if (string == null)
			return null;
		return uppercase ? string.toUpperCase() : string.toLowerCase();
	}

	public QueryOptions copy() {
		return new QueryOptions().pretty(pretty)
			.indent(indent)
			.indentLevel(indentLevel)
			.uppercase(uppercase)
			.dateFormat(dateFormat);
	}

	public String preparedValuesString() {
		StringBuilder sb = new StringBuilder();
		int index = 1;
		String prefix = "";
		for (Object value : preparedValues) {
			sb.append(prefix)
				.append(index++)
				.append(": ")
				.append(value.toString());
			prefix = ", ";
		}
		return sb.toString();
	}

}
