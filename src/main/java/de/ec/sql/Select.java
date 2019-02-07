package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class Select implements QueryPart, Fromable {

	private final List<String> columns = new ArrayList<>();
	private boolean distinct = false;
	private boolean all = false;
	private QueryPart builder;

	public Select() {
	}

	public Select(String... columns) {
		columns(columns);
	}

	protected Select(With with) {
		builder = with;
	}

	protected Select(With with, String... columns) {
		builder = with;
		columns(columns);
	}

	protected Select(Insert insert) {
		builder = insert;
	}

	public Select distinct() {
		distinct = true;
		all = false;
		return this;
	}

	public Select all() {
		distinct = false;
		all = true;
		return this;
	}

	public Select column(String column) {
		columns.add(column);
		return this;
	}

	public Select columns(String... columns) {
		if (columns != null)
			for (String column : columns)
				this.columns.add(column);
		return this;
	}

	@Override
	public From from() {
		return new From(this);
	}

	@Override
	public From from(String... tables) {
		return new From(this, tables);
	}

	@Override
	public String string() {
		return string(QueryOptions.DEFAULT_OPTIONS);
	}

	@Override
	public String string(QueryOptions options) {
		StringJoiner strings = new StringJoiner();

		if (builder != null) {
			strings.add(builder.string(options));
			strings.add(options.newLine());
		}

		strings.add(options.pad("SELECT"));

		if (distinct) {
			strings.add(" ");
			strings.add(options.cased("DISTINCT"));
		} else if (all) {
			strings.add(" ");
			strings.add(options.cased("ALL"));
		}

		if (columns.isEmpty()) {
			strings.add(" *");
		} else {
			StringJoiner columnsStrings = new StringJoiner();
			for (String column : columns)
				columnsStrings.add(QueryUtils.splitName(options, column)
					.string(options));
			strings.add(" ");
			strings.add(columnsStrings.toString(", "));
		}

		return strings.toString();
	}

}
