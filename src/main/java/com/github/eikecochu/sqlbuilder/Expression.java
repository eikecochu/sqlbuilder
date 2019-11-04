package com.github.eikecochu.sqlbuilder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(fluent = true)
public class Expression implements QueryPart {

	private String expression;
	private Object[] values;

	public Expression(final String expression, final Object... values) {
		this.expression = expression;
		this.values = values;
	}

	@Override
	public String toString() {
		return string();
	}

	@Override
	public String string(QueryOptions options) {
		options = safeOptions(options);
		if (options.prepare())
			return preparedString(options);
		else
			return valueString(options);
	}

	public String preparedString(QueryOptions options) {
		options = safeOptions(options);
		String result = expression;
		final boolean prepare = options != null && options.prepare();
		if (values != null && values.length > 0 && !result.contains("(")) {
			final StringBuilder params = new StringBuilder();
			final String defaultPlaceholder = options.defaultPlaceholder();
			final boolean hasPlaceholder = defaultPlaceholder != null && !defaultPlaceholder.isEmpty();
			boolean defaults = false;
			String delim = "";
			for (final Object value : values) {
				if (value == null) {
					defaults = true;
					if (hasPlaceholder)
						params.append(delim)
								.append(defaultPlaceholder);
				} else {
					if (defaults && !hasPlaceholder)
						throw new RuntimeException("middle parameter null");
					params.append(delim)
							.append("?");
					if (prepare)
						options.addPreparedValue(value);
				}
				delim = ", ";
			}
			result += "(" + params.toString() + ")";
		}
		return result;
	}

	public String valueString(QueryOptions options) {
		options = safeOptions(options);
		String result = expression;
		if (values != null && values.length > 0 && !result.contains("(")) {
			final StringBuilder params = new StringBuilder();
			final String defaultPlaceholder = options.defaultPlaceholder();
			final boolean hasPlaceholder = defaultPlaceholder != null && !defaultPlaceholder.isEmpty();
			boolean defaults = false;
			String delim = "";
			for (final Object value : values) {
				if (value == null) {
					defaults = true;
					if (hasPlaceholder)
						params.append(delim)
								.append(defaultPlaceholder);
				} else {
					if (defaults && !hasPlaceholder)
						throw new RuntimeException("middle parameter null");
					params.append(delim)
							.append(QueryUtils.valueToString(options, value));
				}
				delim = ", ";
			}
			result += "(" + params.toString() + ")";
		}
		return result;
	}

	private QueryOptions safeOptions(final QueryOptions options) {
		return options == null ? QueryOptions.getDefaultOptions() : options;
	}

}
