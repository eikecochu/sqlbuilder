package com.github.eikecochu.sqlbuilder;

@FunctionalInterface
public interface Table {

	/**
	 * The table name
	 *
	 * @return The table name
	 */
	String tableName();

}
