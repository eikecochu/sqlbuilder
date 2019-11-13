package com.github.eikecochu.sqlbuilder;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Expressions are used to create stored procedure calls. They can be used as
 * part of a query or as standalone expression.
 */
@Getter
@Setter
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
public class Expression extends QueryPartImpl<Expression> implements QueryBuilder<Expression> {

	private String expression;
	private List<Object> values = new ArrayList<>();
	private List<Integer> paramTypes = new ArrayList<>();
	private int returnType = 0;

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private boolean standalone = false;

	/**
	 * Create a new expression with multiple IN parameters
	 *
	 * @param expression The expression
	 * @param values     The input parameters
	 */
	public Expression(final String expression, final Object... values) {
		this.expression = expression;
		if (values != null)
			for (final Object value : values)
				in(value);
	}

	/**
	 * Add a new IN parameter
	 *
	 * @param value the IN parameter value
	 * @return this instance
	 */
	public Expression in(final Object value) {
		return inOut(value, 0);
	}

	/**
	 * Add a new OUT parameter
	 *
	 * @param type the OUT parameter data type
	 * @return this instance
	 */
	public Expression out(final int type) {
		return inOut(null, type);
	}

	/**
	 * Add a new IN/OUT parameter
	 *
	 * @param value the IN parameter value
	 * @param type  the OUT parameter data type
	 * @return this instance
	 */
	public Expression inOut(final Object value, final int type) {
		values.add(value);
		paramTypes.add(type);
		return this;
	}

	/**
	 * Sets a parameter to be an IN parameter. If it does not exist, parameters will
	 * be added automatically until it exists.
	 *
	 * @param index the parameter index, starting at 0
	 * @param value the IN parameter value
	 * @return this instance
	 */
	public Expression setIn(final int index, final Object value) {
		return setInOut(index, value, 0);
	}

	/**
	 * Set multiple parameters to be IN parameters. The index is taken from the
	 * position of the passed parameters.
	 *
	 * @param values the IN parameter value
	 * @return this instance
	 */
	public Expression setIns(final Object... values) {
		if (values != null)
			for (int i = 0; i < values.length; i++)
				setIn(i, values[i]);
		return this;
	}

	/**
	 * Sets a parameter to be an OUT parameter. If it does not exist, parameters
	 * will be added automatically until it exists.
	 *
	 * @param index the parameter index, starting at 0
	 * @param type  the OUT parameter data type
	 * @return this instance
	 */
	public Expression setOut(final int index, final int type) {
		return setInOut(index, null, type);
	}

	/**
	 * Set multiple parameters to be OUT parameters. The index is taken from the
	 * position of the passed parameters
	 *
	 * @param types the OUT parameter data types
	 * @return this instance
	 */
	public Expression setOuts(final Integer... types) {
		if (types != null)
			for (int i = 0; i < types.length; i++)
				setOut(i, types[i]);
		return this;
	}

	/**
	 * Sets a parameter to be an IN/OUT parameter. If it does not exist, parameters
	 * will be added automatically until it exists.
	 *
	 * @param index the parameter index, starting at 0
	 * @param value the IN parameter value
	 * @param type  the OUT parameter data type
	 * @return this instance
	 */
	public Expression setInOut(final int index, final Object value, final int type) {
		if (index < 0)
			throw new RuntimeException("bad index");
		while (index + 1 > paramTypes.size())
			in(null);
		if (value != null)
			values.set(index, value);
		paramTypes.set(index, type);
		return this;
	}

	@Override
	public String toString() {
		return string();
	}

	@Override
	public String string(final QueryOptions options) {
		return string(options, options.prepare());
	}

	/**
	 * Turns this expression into a string. Will replace values by placeholders if
	 * prepare is true, else it will insert the passed values.
	 *
	 * @param options The QueryOptions
	 * @param prepare True to insert value placeholders, false to insert values
	 * @return The created string
	 */
	public String string(QueryOptions options, final boolean prepare) {
		if (values != null && paramTypes != null && values.size() != paramTypes.size())
			throw new RuntimeException("values and paramTypes size does not match");

		options = safeOptions(options);
		String result = expression;
		if (values != null && values.size() > 0 && !result.contains("(")) {
			// check for OUT parameters
			if (!prepare)
				for (int i = 0; i < values.size(); i++)
					if (values.get(i) == null && paramTypes.get(i) != 0)
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

		if (standalone) {
			result = "call " + result;

			if (returnType != 0)
				result = "? = " + result;

			result = "{ " + result + " }";
		}

		return result;
	}

	/**
	 * Turns this expression into a string with value placeholders for query
	 * preparation
	 *
	 * @param options The QueryOptions
	 * @return The created string
	 */
	public String preparedString(final QueryOptions options) {
		return string(options, true);
	}

	/**
	 * Turns this expression into a string with the passed values inserted.
	 *
	 * @param options The QueryOptions
	 * @return The created string
	 */
	public String valueString(final QueryOptions options) {
		return string(options, false);
	}

	private QueryOptions safeOptions(final QueryOptions options) {
		return options == null ? QueryOptions.getDefaultOptions() : options;
	}

	@Override
	public Query query() {
		standalone = true;
		return new Query(this);
	}

}
