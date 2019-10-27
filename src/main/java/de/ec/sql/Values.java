package de.ec.sql;

import java.util.HashMap;
import java.util.Iterator;

public class Values extends HashMap<String, Object> implements ValueHolder {

	private static final long serialVersionUID = 1L;

	public Values add(String key, Object value) {
		put(key, value);
		return this;
	}

	@Override
	public Iterator<Entry<String, Object>> iterator() {
		return entrySet().iterator();
	}

}
