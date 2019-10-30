package com.github.eikecochu.sqlbuilder;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter(AccessLevel.PROTECTED)
public class InsertValue {

	private final Insert insert;
	private final String column;
	private final List<Object> values = new ArrayList<>();
	private Query query;

	protected InsertValue(final Insert insert, final String column) {
		this.insert = insert;
		this.column = column;
	}

	public Insert value(final Object value) {
		values.add(value);
		query = null;
		return insert;
	}

	public Insert values(final Object... values) {
		if (values != null)
			for (final Object value : values)
				this.values.add(value);
		return insert;
	}

	public Insert query(final Query query) {
		values.clear();
		this.query = query;
		return insert;
	}

}
