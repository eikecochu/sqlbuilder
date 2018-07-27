package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class With implements QueryPart {

	private With with;
	private final String name;
	private final List<String> columns = new ArrayList<>();
	private Query query;
	private boolean recursive;

	public With(String name) {
		this.name = name;
	}

	private With(With with, String name) {
		this.name = name;
		this.with = with;
	}

	public With recursive() {
		recursive = true;
		return this;
	}

	public With column(String column) {
		columns.add(column);
		return this;
	}

	public With columns(String... columns) {
		for (String column : columns)
			this.columns.add(column);
		return this;
	}

	public With as(Query query) {
		this.query = query;
		return this;
	}

	public Select select() {
		return new Select(this);
	}

	public Select select(String... columns) {
		return new Select(this, columns);
	}

	public Delete delete(String table) {
		return new Delete(this, table);
	}

	public Delete delete(Table table) {
		return delete(table.tableName());
	}

	public Update update(String table) {
		return new Update(this, table);
	}

	public Update update(Table table) {
		return update(table.tableName());
	}

	public With with(String name) {
		return new With(this, name);
	}

	@Override
	public String string() {
		return string(QueryOptions.DEFAULT_OPTIONS);
	}

	@Override
	public String string(QueryOptions options) {
		StringJoiner strings = new StringJoiner();

		if (with == null) {
			strings.add(options.pad("WITH"));

			if (recursive) {
				strings.add(" ");
				strings.add(options.cased("RECURSIVE"));
			}
		} else {
			strings.add(options.newLine());
			strings.add(with.string(options));
			strings.add(",");
		}

		strings.add(" ");
		strings.add(name);

		if (!columns.isEmpty()) {
			strings.add(" (");
			strings.add(StringUtils.join(columns, ", "));
			strings.add(")");
		}

		strings.add(" ");
		strings.add(options.cased("AS"));
		strings.add(" (");

		QueryOptions subOptions = options.copy()
			.indentLevel(options.indentLevel() + 1);
		strings.add(subOptions.newLine());
		strings.add(query.string(subOptions)
			.trim());

		strings.add(")");

		return strings.toString();
	}

}
