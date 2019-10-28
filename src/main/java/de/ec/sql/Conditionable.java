package de.ec.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public abstract class Conditionable<T extends Conditionable<T>> implements QueryPart {

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

	@SuppressWarnings("unchecked")
	public T values(final ValueHolder values) {
		if (values != null)
			for (final Entry<String, Object> value : values)
				col(value.getKey(), value.getValue());
		return (T) this;
	}

	public ConditionPart<T> col(final String name) {
		if (!parts.isEmpty() && !(parts.get(parts.size() - 1) instanceof Operator))
			this.and();

		final ConditionPart<T> part = new ConditionPart<>(conditionable(), name);
		parts.add(part);
		return part;
	}

	public T col(final String name, final Object value) {
		return col(name).eq(value);
	}

	public T col(final String name, final Object... values) {
		return col(name).in(values);
	}

	@SuppressWarnings("unchecked")
	public T group(final Condition group) {
		if (!parts.isEmpty() && !(parts.get(parts.size() - 1) instanceof Operator))
			this.and();

		parts.add(group);
		return (T) conditionable;
	}

	public Conditionable<T> and() {
		if (!parts.isEmpty() && parts.size() % 2 == 1)
			parts.add(Operator.AND);
		return this;
	}

	public Conditionable<T> or() {
		if (!parts.isEmpty() && parts.size() % 2 == 1)
			parts.add(Operator.OR);
		return this;
	}

	private Conditionable<T> conditionable() {
		return conditionable == null ? (Conditionable<T>) this : conditionable;
	}

	@Override
	public String string(final QueryOptions options) {
		if (parts.isEmpty())
			return null;

		if (parts.size() % 2 != 1)
			throw new RuntimeException("wrong part count");

		final StringJoiner strings = new StringJoiner();

		final String first = parts.get(0)
				.string(options);
		boolean needsOp = false;

		if (first != null && !first.isEmpty()) {
			needsOp = true;
			strings.add(first);
		}

		for (int i = 1; i < parts.size(); i += 2) {
			final String op = parts.get(i)
					.string(options);
			final String val = parts.get(i + 1)
					.string(options);

			if (needsOp) {
				if (op != null && !op.isEmpty() && val != null && !val.isEmpty())
					strings.add(op)
							.add(val);
			} else {
				if (val != null && !val.isEmpty()) {
					needsOp = true;
					strings.add(val);
				}
			}
		}

		return strings.toString(" ");
	}

}
