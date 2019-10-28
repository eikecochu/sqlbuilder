package de.ec.sql;

public interface BeforeGroupBy extends QueryPart {

	default GroupBy groupBy() {
		return new GroupBy(this);
	}

	default GroupBy groupBy(final String... columns) {
		return groupBy().columns(columns);
	}

	default GroupBy groupBy(final GroupBy groupBy) {
		return groupBy.builder(this);
	}

	default GroupBy groupBySQL(final String sql) {
		return groupBy().sql(sql);
	}

}
