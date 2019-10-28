package de.ec.sql;

public interface BeforeSelect extends QueryPart {

	default Select select() {
		return new Select(this);
	}

	default Select select(String... columns) {
		return select().columns(columns);
	}

	default Select select(Select select) {
		return select.builder(this);
	}

}
