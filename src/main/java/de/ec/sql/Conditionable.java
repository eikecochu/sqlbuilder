package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

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

	protected Conditionable(Conditionable<T> conditionable) {
		this.conditionable = conditionable;
	}

	public ConditionPart<T> col(String name) {
		assert (parts.isEmpty() || parts.get(parts.size() - 1) instanceof Operator);

		ConditionPart<T> part = new ConditionPart<>(conditionable(), name);
		parts.add(part);
		return part;
	}

	public ConditionPart<T> col(String tableAlias, String name) {
		return col(tableAlias + "." + name);
	}

	public T colPair(String name1, String name2) {
		return col(name1).eq()
			.col(name2);
	}

	public T col(String name, Object value) {
		return col(name).eq(value);
	}

	public T col(String name, Object... values) {
		return col(name).in(values);
	}

	@SuppressWarnings("unchecked")
	public T group(Condition group) {
		assert (parts.isEmpty() || parts.get(parts.size() - 1) instanceof Operator);

		parts.add(group);
		return (T) conditionable;
	}

	public Conditionable<T> and() {
		assert !parts.isEmpty() : "operator can not be the first part of a condition";
		assert parts.size() % 2 == 1 : "condition malformed: parts of a condition must be separated by operators";
		assert !(parts.get(parts.size() - 1) instanceof Operator) : "operator can not immediately follow an operator";

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

		StringJoiner strings = new StringJoiner();

		for (QueryPart part : parts) {
			String condition = part.string(options);
			if (condition != null && !condition.isEmpty())
				strings.add(condition);
		}

		return strings.toString(" ");
	}

}
