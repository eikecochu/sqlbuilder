package de.ec.sql;

import java.util.Map.Entry;

@FunctionalInterface
public interface ValueHolder {

	Iterable<Entry<String, Object>> values();

}
