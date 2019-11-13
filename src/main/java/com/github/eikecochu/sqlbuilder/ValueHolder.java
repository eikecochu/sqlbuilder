package com.github.eikecochu.sqlbuilder;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The ValueHolder instance allows to declare an arbitrary class as a
 * ValueHolder to be used in various methods of the SQLBuilder, for example
 * Where.where(ValueHolder). Instead of passing all values of an object
 * manually, this interface can be used to extract multiple values at once from
 * any object of classes that implement it.
 */
public interface ValueHolder extends Iterable<Entry<String, Object>> {

	/**
	 * The ValueEntry class is an implementation of the Map.Entry class
	 */
	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ValueEntry implements Entry<String, Object> {

		private String key;
		private Object value;

		@Override
		public Object setValue(final Object value) {
			final Object prev = value;
			this.value = value;
			return prev;
		}

	}

	/**
	 * The EntryWrapper is a wrapper around the Map.Entry class to support automatic
	 * key prefixing.
	 */
	@AllArgsConstructor
	public static class EntryWrapper implements Entry<String, Object> {

		private final Entry<String, Object> entry;
		private final String prefix;

		@Override
		public String getKey() {
			return prefix + "." + entry.getKey();
		}

		@Override
		public Object getValue() {
			return entry.getValue();
		}

		@Override
		public Object setValue(final Object value) {
			return entry.setValue(value);
		}
	}

	/**
	 * Create a ValueHolder from an Iterable instance
	 *
	 * @param it The Iterable
	 * @return the ValueHolder instance
	 */
	static ValueHolder from(final Iterable<? extends Entry<String, Object>> it) {
		return (ValueHolder) it;
	}

	/**
	 * Creates a ValueHolder instance from a Map instance
	 *
	 * @param map The Map
	 * @return the ValueHolder instance
	 */
	static ValueHolder from(final Map<String, Object> map) {
		return map == null ? null : from(map.entrySet());
	}

	/**
	 * Creates a new Map.Entry instance
	 *
	 * @param string the key
	 * @param object the value
	 * @return the new Map.Entry instance
	 */
	static Entry<String, Object> entry(final String string, final Object object) {
		return new ValueEntry(string, object);
	}

	@Override
	default Iterator<Entry<String, Object>> iterator() {
		return null;
	}

	/**
	 * Returns the values stored in this ValueHolder. Optionally prefixes the keys
	 * with a column prefix.
	 *
	 * @param columnPrefix The optional columnPrefix. Pass null to disable. Pass a
	 *                     value to prefix all keys with this prefix. The prefix and
	 *                     the key are joined with a "."
	 * @return The value iterator
	 */
	default Iterator<Entry<String, Object>> values(final String columnPrefix) {
		final Iterator<Entry<String, Object>> it = iterator();
		if (it == null || columnPrefix == null || columnPrefix.isEmpty())
			return it;
		return new Iterator<Entry<String, Object>>() {

			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public Entry<String, Object> next() {
				return new EntryWrapper(it.next(), columnPrefix);
			}

		};
	}

}
