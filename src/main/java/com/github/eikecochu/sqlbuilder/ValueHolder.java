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

	static ValueHolder from(final Iterable<? extends Entry<String, Object>> it) {
		return (ValueHolder) it;
	}

	static ValueHolder from(final Map<String, Object> map) {
		return map == null ? null : from(map.entrySet());
	}

	static Entry<String, Object> entry(final String string, final Object object) {
		return new ValueEntry(string, object);
	}

	@Override
	default Iterator<Entry<String, Object>> iterator() {
		return null;
	}

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
