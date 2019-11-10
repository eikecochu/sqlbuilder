package com.github.eikecochu.sqlbuilder;

import java.util.HashMap;
import java.util.Iterator;

public class Values extends HashMap<String, Object> implements ValueHolder {

	private static final long serialVersionUID = 1L;

	/**
	 * Add a value
	 *
	 * @param key   The key of the value
	 * @param value The value
	 * @return This Values instance
	 */
	public Values add(final String key, final Object value) {
		put(key, value);
		return this;
	}

	@Override
	public Iterator<Entry<String, Object>> iterator() {
		return entrySet().iterator();
	}

}
