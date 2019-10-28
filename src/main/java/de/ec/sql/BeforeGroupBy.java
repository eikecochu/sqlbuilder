package de.ec.sql;

public interface BeforeGroupBy extends QueryPart {

	default GroupBy groupBy() {
		return new GroupBy(this);
	}

	default GroupBy groupBy(String... columns) {
		return groupBy().columns(columns);
	}

	default GroupBy groupBy(GroupBy groupBy) {
		return groupBy.builder(this);
	}

}
