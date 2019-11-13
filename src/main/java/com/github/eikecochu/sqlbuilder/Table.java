package com.github.eikecochu.sqlbuilder;

/**
 * The Table interface is a representation of a table with a name.
 */
@FunctionalInterface
public interface Table {

	/**
	 * The table name
	 *
	 * @return The table name
	 */
	String tableName();

}
