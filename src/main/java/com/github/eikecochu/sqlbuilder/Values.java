package com.github.eikecochu.sqlbuilder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
	public Iterator<Entry<String, Object>> values(final String columnPrefix) {
		Set<Entry<String, Object>> set = super.entrySet();
		if (columnPrefix != null && !columnPrefix.trim()
				.isEmpty()) {
			final Map<String, Object> map = new HashMap<>();
			for (final Entry<String, Object> entry : set)
				map.put(columnPrefix + "." + entry.getKey(), entry.getValue());
			set = map.entrySet();
		}
		return set.iterator();
	}

}
