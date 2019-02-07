package de.ec.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public abstract class Conditionable<T extends Conditionable<T>> implements QueryPart {

	public static enum Operator implements QueryPart {
		AND("AND"), OR("OR");

		private final String string;

		private Operator(String string) {
			this.string = string;
		}

		@Override
		public String string() {
			return string(QueryOptions.DEFAULT_OPTIONS);
		}

		@Override
		public String string(QueryOptions options) {
			return options.cased(string);
		}
	}

	private final List<QueryPart> parts = new ArrayList<>();
	private final Conditionable<T> conditionable;

	public Conditionable() {
		this.conditionable = this;
	}

	@SuppressWarnings("unchecked")
	public T values(ValueHolder values) {
		if (values != null)
			for (Entry<String, Object> value : values.values())
				col(value.getKey(), value.getValue());
		return (T) this;
	}

	public ConditionPart<T> col(String name) {
		if (!parts.isEmpty() && !(parts.get(parts.size() - 1) instanceof Operator))
			this.and();

		ConditionPart<T> part = new ConditionPart<>(conditionable(), name);
		parts.add(part);
		return part;
	}

	public T col(String name, Object value) {
		return col(name).eq(value);
	}

	public T col(String name, Object... values) {
		return col(name).in(values);
	}

	@SuppressWarnings("unchecked")
	public T group(Condition group) {
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
	public String string() {
		return string(QueryOptions.DEFAULT_OPTIONS);
	}

	@Override
	public String string(QueryOptions options) {
		if (parts.isEmpty())
			return null;

		if (parts.size() % 2 != 1)
			throw new RuntimeException("wrong part count");

		StringJoiner strings = new StringJoiner();

		String first = parts.get(0).string(options);
		boolean needsOp = false;

		if (first != null && !first.isEmpty()) {
			needsOp = true;
			strings.add(first);
		}

		for (int i = 1; i < parts.size(); i += 2) {
			String op = parts.get(i).string(options);
			String val = parts.get(i + 1).string(options);

			if (needsOp) {
				if (op != null && !op.isEmpty() && val != null && !val.isEmpty())
					strings.add(op).add(val);
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
