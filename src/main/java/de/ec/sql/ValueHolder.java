package de.ec.sql;

import java.util.Iterator;
import java.util.Map.Entry;

public interface ValueHolder extends Iterable<Entry<String, Object>>, Iterator<Entry<String, Object>> {

	@Override
	default Iterator<Entry<String, Object>> iterator() {
		return iterator();
	}

}
