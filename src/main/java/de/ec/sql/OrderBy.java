package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class OrderBy implements QueryBuilder, QueryPart {

	private final Having having;
	private final List<OrderByTerm> orderByTerms = new ArrayList<>();

	protected OrderBy(GroupBy groupBy) {
		having = new Having(groupBy);
	}

	protected OrderBy(GroupBy groupBy, String... names) {
		this(groupBy);
		columns(names);
	}

	protected OrderBy(Having having) {
		this.having = having;
	}

	public OrderBy column(String name, boolean ascending) {
		orderByTerms.add(new OrderByTerm(name, ascending));
		return this;
	}

	public OrderBy column(String name) {
		if (name != null) {
			String nameUpper = name.toUpperCase()
				.trim();
			boolean ascending = true;
			if (nameUpper.endsWith(" DESC")) {
				ascending = false;
				name = name.replaceAll("\\s+[Dd][Ee][Ss][Cc]\\s*$", "");
			} else {
				name = name.replaceAll("\\s+[Aa][Ss][Cc]\\s*$", "");
			}
			String schema = null;
			if (name.contains(".")) {
				String[] parts = name.split("\\.");
				schema = parts[0];
				name = parts[1];
			}
			orderByTerms.add(new OrderByTerm(schema, name, ascending));
		}
		return this;
	}

	public OrderBy columns(String... names) {
		if (names != null)
			for (String name : names)
				column(name);
		return this;
	}

	public OrderBy asc(String name) {
		return column(name, true);
	}

	public OrderBy desc(String name) {
		return column(name, false);
	}

	@Override
	public Query query() {
		return new Query(this);
	}

	@Override
	public String string() {
		return string(QueryOptions.DEFAULT_OPTIONS);
	}

	@Override
	public String string(QueryOptions options) {
		StringJoiner strings = new StringJoiner();

		strings.add(having.string(options));

		StringJoiner orderStrings = new StringJoiner();
		for (OrderByTerm orderByTerm : orderByTerms)
			orderStrings.add(orderByTerm.string(options));

		if (!orderStrings.isEmpty()) {
			strings.add(options.newLine());
			strings.add(options.pad("ORDER BY"));
			strings.add(" ");
			strings.add(orderStrings.toString(", "));
		}

		return strings.toString();
	}

}
