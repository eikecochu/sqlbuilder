package de.ec.sql;

public interface BeforeWhere extends QueryPart {

	default Where where() {
		return new Where(this);
	}

	default Where where(final ValueHolder values) {
		return where().values(values);
	}

	default Where where(final Where where) {
		return where.builder(this);
	}

	default Where whereSQL(final String sql) {
		return where().sql(sql);
	}

}
