package de.ec.sql;

import de.ec.sql.before.BeforeWhere;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PROTECTED)
public class Delete implements QueryBuilder, QueryPart, BeforeWhere {

	@Setter(AccessLevel.PACKAGE)
	private With with;
	private final String table;

	public Delete(final String table) {
		this.table = table;
	}

	public Delete(final Table table) {
		this(table.tableName());
	}

	protected Delete(final With with, final String table) {
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
	public Where where(final ValueHolder values) {
		return where().values(values);
	}

	@Override
	public Where where(final Where where) {
		where.setBuilder(this);
		return where;
	}

	@Override
	public String string() {
		return string(QueryOptions.DEFAULT_OPTIONS);
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

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
