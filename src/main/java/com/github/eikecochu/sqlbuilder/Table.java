package com.github.eikecochu.sqlbuilder;

import java.util.function.Supplier;

@FunctionalInterface
public interface Table extends Supplier<String> {

	/**
	 * The table name
	 *
	 * @return The table name
	 */
	default String tableName() {
		return get();
	}

}
