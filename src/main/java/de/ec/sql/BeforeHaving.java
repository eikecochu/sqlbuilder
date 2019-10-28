package de.ec.sql;

public interface BeforeHaving extends QueryPart {

	default Having having() {
		return new Having(this);
	}

	default Having having(final Having having) {
		return having.builder(this);
	}

	default Having havingSQL(final String sql) {
		return having().sql(sql);
	}

}
