package de.ec.sql;

import java.util.function.Supplier;

@FunctionalInterface
public interface Table extends Supplier<String> {

	default String tableName() {
		return get();
	}

}
