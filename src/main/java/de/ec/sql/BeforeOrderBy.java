package de.ec.sql;

public interface BeforeOrderBy extends QueryPart {

	default OrderBy orderBy() {
		return new OrderBy(this);
	}

	default OrderBy orderBy(String... columns) {
		return orderBy().columns(columns);
	}

	default OrderBy orderBy(OrderBy orderBy) {
		return orderBy.builder(this);
	}

}
