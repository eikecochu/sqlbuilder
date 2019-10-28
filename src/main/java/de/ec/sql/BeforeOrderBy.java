package de.ec.sql;

public interface BeforeOrderBy extends QueryPart {

	default OrderBy orderBy() {
		return new OrderBy(this);
	}

	default OrderBy orderBy(final String... columns) {
		return orderBy().columns(columns);
	}

	default OrderBy orderBy(final OrderBy orderBy) {
		return orderBy.builder(this);
	}

	default OrderBy orderBySQL(final String sql) {
		return orderBy().sql(sql);
	}

}
