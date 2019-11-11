package com.github.eikecochu.sqlbuilder;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
public class Expression extends QueryPartImpl<Expression> implements QueryBuilder<Expression> {

	private String expression;
	private List<Object> values;
	private List<Integer> paramTypes;
	private Integer returnType;

	public Expression(final String expression, final Object... values) {
		this.expression = expression;
		if (values != null)
			for (final Object value : values)
				in(value);
	}

	public Expression in(final Object value) {
		return inOut(value, null);
	}

	public Expression out(final int type) {
		return inOut(null, type);
	}

	public Expression inOut(final Object value, final Integer type) {
		if (values == null)
			values = new ArrayList<>();
		values.add(value);
		if (paramTypes == null)
			paramTypes = new ArrayList<>();
		paramTypes.add(type);
		return this;
	}

	@Override
	public String toString() {
		return string();
	}

	@Override
	public String string(final QueryOptions options) {
		return string(options, options.prepare(), false);
	}

	public String string(QueryOptions options, final boolean prepare, final boolean callWrap) {
		if (values != null && paramTypes != null && values.size() != paramTypes.size())
			throw new RuntimeException("values and paramTypes size does not match");

		options = safeOptions(options);
		String result = expression;
		if (values != null && values.size() > 0 && !result.contains("(")) {
			// check for OUT parameters
			if (!prepare)
				for (int i = 0; i < values.size(); i++)
					if (values.get(i) == null && paramTypes.get(i) != null)
						throw new RuntimeException("non-prepare method does not support OUT parameters");

			// get default placeholder
			final String defaultPlaceholder = options.defaultPlaceholder();
			final boolean hasPlaceholder = defaultPlaceholder != null && !defaultPlaceholder.isEmpty();

			final StringBuilder params = new StringBuilder();
			String delim = "";
			boolean defaults = false;
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
							.append(prepare ? "?" : value);
					if (prepare)
						options.addPreparedValue(value);
				}

				delim = ", ";
			}

			final String paramString = params.toString();
			if (!paramString.isEmpty())
				result += "(" + paramString + ")";
		}

		if (callWrap) {
			result = "call " + result;

			if (prepare && returnType != null)
				result = "? = " + result;

			result = "{ " + result + " }";
		}

		return result;
	}

	public String preparedString(final QueryOptions options) {
		return string(options, true, false);
	}

	public String valueString(final QueryOptions options) {
		return string(options, false, false);
	}

	private QueryOptions safeOptions(final QueryOptions options) {
		return options == null ? QueryOptions.getDefaultOptions() : options;
	}

}
