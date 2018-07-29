package de.ec.sql;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class Delete implements QueryBuilder, QueryPart, Whereable {

	private With with;
	private String table;

	public Delete(String table) {
		this.table = table;
	}

	public Delete(Table table) {
		this(table.tableName());
	}

	protected Delete(With with, String table) {
		this(table);
		this.with = with;
	}

	@Override
	public Query query() {
		return new Query(this);
	}

	@Override
	public Where where() {
		return new Where(this);
	}
	
	@Override
	public Where where(ValueHolder values) {
		return where().values(values);
	}

	@Override
	public String string() {
		return string(QueryOptions.DEFAULT_OPTIONS);
	}

	@Override
	public String string(QueryOptions options) {
		StringJoiner strings = new StringJoiner();

		if (with != null) {
			strings.add(with.string(options));
			strings.add(options.newLine());
		}

		strings.add(options.pad("DELETE FROM"));

		strings.add(" ");
		strings.add(table);

		return strings.toString();
	}

}
