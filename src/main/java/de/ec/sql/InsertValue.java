package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class InsertValue {

	private final Insert insert;
	private final String column;
	private List<Object> values = new ArrayList<>();
	private Query query;

	protected InsertValue(Insert insert, String column) {
		this.insert = insert;
		this.column = column;
	}

	public Insert value(Object value) {
		values.add(value);
		query = null;
		return insert;
	}

	public Insert values(Object... values) {
		if (values != null)
			for (Object value : values)
				this.values.add(value);
		return insert;
	}

	public Insert query(Query query) {
		values.clear();
		this.query = query;
		return insert;
	}

}
