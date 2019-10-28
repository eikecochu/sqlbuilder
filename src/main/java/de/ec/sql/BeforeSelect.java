package de.ec.sql;

public interface BeforeSelect extends QueryPart {

	default Select select() {
		return new Select(this);
	}

	default Select select(final String... columns) {
		return select().columns(columns);
	}

	default Select select(final Select select) {
		return select.builder(this);
	}

	default Select selectSQL(final String sql) {
		return select().sql(sql);
	}

}
