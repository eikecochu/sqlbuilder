package com.github.eikecochu.sqlbuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

/**
 * Abstract base class for condition like statements, generic to allow multiple
 * origins, for example from WHERE, JOIN etc.
 *
 * @param <T> The type which allows setting conditions
 */
@ToString
@Getter(AccessLevel.PROTECTED)
public abstract class Conditionable<T extends Conditionable<T>> extends SQLQueryPart<T> {

	@ToString
	public enum Operator implements QueryPart {
		AND("AND"),
		OR("OR");

		private final String string;

		private Operator(final String string) {
			this.string = string;
		}

		@Override
		public String string(final QueryOptions options) {
			return options.cased(string);
		}
	}

	private final List<QueryPart> parts = new ArrayList<>();
	private final Conditionable<T> conditionable;

	public Conditionable() {
		this.conditionable = this;
	}

	/**
	 * Set the comparing values
	 *
	 * @param values The comparing values
	 * @return This instance
	 */
	@SuppressWarnings("unchecked")
	public T values(final ValueHolder values) {
		if (values != null)
			for (final Entry<String, Object> value : values)
				col(value.getKey(), value.getValue());
		return (T) this;
	}

	/**
	 * Use a column as constraint
	 *
	 * @param name The column name
	 * @return The value instance to set the compare value
	 */
	public ConditionPart<T> col(final String name) {
		final ConditionPart<T> part = new ConditionPart<>(conditionable(), name);
		addPart(part);
		return part;
	}

	/**
	 * Compare a column to a value
	 *
	 * @param name  The column name
	 * @param value The comparing value
	 * @return This instance
	 */
	public T col(final String name, final Object value) {
		return col(name).eq(value);
	}

	/**
	 * Compare a column to multiple values
	 *
	 * @param name   The column name
	 * @param values The comparing values
	 * @return This instance
	 */
	public T col(final String name, final Object... values) {
		return col(name).in(values);
	}

	/**
	 * Include a subgroup in the condition
	 *
	 * @param group The subgroup
	 * @return This instance
	 */
	public T group(final Condition group) {
		return addPart(group);
	}

	/**
	 * Start a nested condition group
	 *
	 * @return The new nested condition group
	 */
	public NestedCondition<T> groupStart() {
		final NestedCondition<T> group = new NestedCondition<>(this);
		addPart(group);
		return group;
	}

	/**
	 * Connect the previous and next condition with the AND operator
	 *
	 * @return This instance
	 */
	public T and() {
		return op(Operator.AND);
	}

	/**
	 * Connect the previous and next condition with the OR operator
	 *
	 * @return This instance
	 */
	public T or() {
		return op(Operator.OR);
	}

	@SuppressWarnings("unchecked")
	T addPart(final QueryPart part) {
		if (!parts.isEmpty() && !(parts.get(parts.size() - 1) instanceof Operator))
			this.and();

		parts.add(part);
		return (T) conditionable;
	}

	@SuppressWarnings("unchecked")
	private T op(final Operator operator) {
		if (!parts.isEmpty() && parts.size() % 2 == 1)
			parts.add(operator);
		return (T) this;
	}

	private Conditionable<T> conditionable() {
		return conditionable == null ? (Conditionable<T>) this : conditionable;
	}

	@Override
	public String string(final QueryOptions options) {
		if (sql() != null)
			return sql();

		// check for alternating value - operator pairs
		final List<QueryPart> validParts = new ArrayList<>(parts.size());
		boolean valueNext = true;
		for (final QueryPart part : parts) {
			if (valueNext == !(part instanceof Operator))
				validParts.add(part);
			valueNext = !valueNext;
		}

		// remove trailing operators
		while (!parts.isEmpty() && parts.get(parts.size() - 1) instanceof Operator)
			parts.remove(parts.size() - 1);

		// check if empty
		if (parts.isEmpty())
			return null;

		final StringJoiner strings = new StringJoiner();

		// get the first part
		final String first = parts.get(0)
				.string(options);
		boolean needsOp = false;

		if (first != null && !first.isEmpty()) {
			needsOp = true;
			strings.add(first);
		}

		// loop through remaining parts
		// this makes sure that not only values and operators alternate, but it also
		// ensures that parts which evaluate to "" are handled and following operators
		// are ignored
		for (int i = 1; i < parts.size(); i += 2) {
			final String op = parts.get(i)
					.string(options);
			final String val = parts.get(i + 1)
					.string(options);

			if (needsOp) {
				if (op != null && !op.isEmpty() && val != null && !val.isEmpty()) {
					if (options.conditionOnNewline()) {
						strings.add(options.newLine());
						strings.add(options.padded(op));
						strings.add(" ");
					} else
						strings.add(op);
					strings.add(val);
				}
			} else if (val != null && !val.isEmpty()) {
				needsOp = true;
				strings.add(val);
			}
		}

		return strings.toString(options.conditionOnNewline() ? "" : " ");

	}

}
