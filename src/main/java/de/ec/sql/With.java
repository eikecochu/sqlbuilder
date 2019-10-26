package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.ec.sql.before.BeforeDelete;
import de.ec.sql.before.BeforeInsert;
import de.ec.sql.before.BeforeSelect;
import de.ec.sql.before.BeforeUpdate;
import de.ec.sql.before.BeforeWith;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PROTECTED)
public class With implements QueryPart, BeforeWith, BeforeSelect, BeforeUpdate, BeforeDelete, BeforeInsert {

	@Setter(AccessLevel.PACKAGE)
	private With with;
	private final String name;
	private final List<String> columns = new ArrayList<>();
	private Query query;
	private boolean recursive;

	public With(final String name) {
		this.name = name;
	}

	private With(final With with, final String name) {
		this.name = name;
		this.with = with;
	}

	public With recursive() {
		recursive = true;
		return this;
	}

	public With column(final String column) {
		columns.add(column);
		return this;
	}

	public With columns(final String... columns) {
		for (final String column : columns)
			this.columns.add(column);
		return this;
	}

	public With as(final Query query) {
		this.query = query;
		return this;
	}

	@Override
	public Select select() {
		return new Select(this);
	}

	@Override
	public Select select(final String... columns) {
		return new Select(this, columns);
	}

	@Override
	public Select select(final Select select) {
		select.setBuilder(this);
		return select;
	}

	@Override
	public Delete delete(final String table) {
		return new Delete(this, table);
	}

	@Override
	public Delete delete(final Table table) {
		return delete(table.tableName());
	}

	@Override
	public Delete delete(final Delete delete) {
		delete.setWith(this);
		return delete;
	}

	@Override
	public Update update(final String table) {
		return new Update(this, table);
	}

	@Override
	public Update update(final Table table) {
		return update(table.tableName());
	}

	@Override
	public Update update(final Update update) {
		update.setWith(this);
		return update;
	}

	@Override
	public Insert insert(final String table) {
		return new Insert(this, table);
	}

	@Override
	public Insert insert(final Table table) {
		return insert(table.tableName());
	}

	@Override
	public Insert insert(final Insert insert) {
		insert.setWith(this);
		return insert;
	}

	@Override
	public With with(final String name) {
		return new With(this, name);
	}

	@Override
	public With with(final With with) {
		with.setWith(this);
		return with;
	}

	@Override
	public String string() {
		return string(QueryOptions.DEFAULT_OPTIONS);
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

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

		final QueryOptions subOptions = options.copy()
				.indentLevel(options.indentLevel() + 1);
		strings.add(subOptions.newLine());
		strings.add(query.string(subOptions)
				.trim());

		strings.add(")");

		return strings.toString();
	}

}
