package com.github.eikecochu.sqlbuilder;

import lombok.ToString;

@ToString
public enum InsertMode implements QueryPart {
	INSERT("INSERT INTO"),
	REPLACE("REPLACE INTO"),
	INSERT_OR_REPLACE("INSERT OR REPLACE INTO"),
	INSERT_OR_ROLLBACK("INSERT OR ROLLBACK INTO"),
	INSERT_OR_ABORT("INSERT OR ABORT INTO"),
	INSERT_OR_FAIL("INSERT OR FAIL INTO"),
	INSERT_OR_IGNORE("INSERT OR IGNORE INTO");

	private final String string;

	InsertMode(final String string) {
		this.string = string;
	}

	@Override
	public String string(final QueryOptions options) {
		return options.cased(string);
	}
}