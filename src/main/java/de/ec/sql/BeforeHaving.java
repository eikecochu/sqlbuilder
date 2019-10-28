package de.ec.sql;

public interface BeforeHaving extends QueryPart {

	default Having having() {
		return new Having(this);
	}

	default Having having(Having having) {
		return having.builder(this);
	}

}
