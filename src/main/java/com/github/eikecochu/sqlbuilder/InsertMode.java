package com.github.eikecochu.sqlbuilder;

import lombok.ToString;

/**
 * InsertMode defines how the INSERT statement inserts values into the database.
 */
@ToString
public enum InsertMode implements QueryPart {
	/**
	 * The INSERT mode. Default behavior. Will attempt to insert values into the
	 * database.
	 */
	INSERT("INSERT INTO"),

	/**
	 * The REPLACE mode. Will attempt to replace values in the database.
	 */
	REPLACE("REPLACE INTO"),

	/**
	 * The INSERT OR REPLACE mode. Will attempt to replace values in the database if
	 * they exist, otherwise it attempts to insert them.
	 */
	INSERT_OR_REPLACE("INSERT OR REPLACE INTO"),

	/**
	 * The INSERT OR ROLLBACK mode. Will attempt to insert values into the database
	 * and rollback changes if any insertion fails.
	 */
	INSERT_OR_ROLLBACK("INSERT OR ROLLBACK INTO"),

	/**
	 * The INSERT OR ABORT mode. Will attempt to insert values into the database and
	 * stop of any insertion fails.
	 */
	INSERT_OR_ABORT("INSERT OR ABORT INTO"),

	/**
	 * The INSERT OR FAIL mode. Will attempt to insert values into the database and
	 * fail if any insertion fails.
	 */
	INSERT_OR_FAIL("INSERT OR FAIL INTO"),

	/**
	 * The INSERT OR IGNORE mode. Will attempt to insert values into the database
	 * and ignore any failing insertion.
	 */
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