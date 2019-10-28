package de.ec.sql;

public interface BeforeWhere extends QueryPart {

	default Where where() {
		return new Where(this);
	}

	default Where where(ValueHolder values) {
		return where().values(values);
	}

	default Where where(Where where) {
		return where.builder(this);
	}

}
