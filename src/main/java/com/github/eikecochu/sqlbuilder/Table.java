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

	/**
	 * The table name with alias
	 * 
	 * @param alias The alias
	 * @return The table name with alias
	 */
	default String withAlias(String alias) {
		return tableName() + (alias != null && !alias.isEmpty() ? (" " + alias) : "");
	}

}
