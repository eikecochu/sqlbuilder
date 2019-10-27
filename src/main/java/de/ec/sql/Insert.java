package de.ec.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import de.ec.sql.Keyword.PrimaryKeyword;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PROTECTED)
public class Insert implements QueryBuilder, BeforeSelect, PrimaryKeyword {

	private enum InsertType implements QueryPart {
		INSERT("INSERT INTO"),
		REPLACE("REPLACE INTO"),
		INSERT_OR_REPLACE("INSERT OR REPLACE INTO"),
		INSERT_OR_ROLLBACK("INSERT OR ROLLBACK INTO"),
		INSERT_OR_ABORT("INSERT OR ABORT INTO"),
		INSERT_OR_FAIL("INSERT OR FAIL INTO"),
		INSERT_OR_IGNORE("INSERT OR IGNORE INTO");

		private String string;

		private InsertType(final String string) {
			this.string = string;
		}

		@Override
		public String string() {
			return string(QueryOptions.DEFAULT_OPTIONS);
		}

		@Override
		public String string(final QueryOptions options) {
			return options.cased(string);
		}
	}

	@Setter(AccessLevel.PACKAGE)
	private With with;
	private final String table;
	private InsertType insertType = InsertType.INSERT;
	private final List<InsertValue> insertValues = new ArrayList<>();
	private boolean defaultValues = false;
	private boolean onlyValues = true;

	public Insert(final String table) {
		this.table = table;
	}

	public Insert(final Table table) {
		this(table.tableName());
	}

	protected Insert(final With with, final String table) {
		this(table);
		this.with = with;
	}

	protected Insert setType(final InsertType type) {
		insertType = type;
		return this;
	}

	public Insert replace() {
		return setType(InsertType.REPLACE);
	}

	public Insert orReplace() {
		return setType(InsertType.INSERT_OR_REPLACE);
	}

	public Insert orRollback() {
		return setType(InsertType.INSERT_OR_ROLLBACK);
	}

	public Insert orAbort() {
		return setType(InsertType.INSERT_OR_ABORT);
	}

	public Insert orFail() {
		return setType(InsertType.INSERT_OR_FAIL);
	}

	public Insert orIgnore() {
		return setType(InsertType.INSERT_OR_IGNORE);
	}

	@Override
	public Select select() {
		insertValues.clear();
		defaultValues = false;
		return new Select(this);
	}

	@Override
	public Select select(final String... columns) {
		return select().columns(columns);
	}

	@Override
	public Select select(final Select select) {
		select.setBuilder(this);
		return select;
	}

	public InsertValue column(final String column) {
		defaultValues = false;
		onlyValues = false;
		final InsertValue insertValue = new InsertValue(this, column);
		insertValues.add(insertValue);
		return insertValue;
	}

	public Insert column(final String column, final Object value) {
		return column(column).value(value);
	}

	public Insert columns(final ValueHolder values) {
		for (final Entry<String, Object> entry : values)
			column(entry.getKey(), entry.getValue());
		return this;
	}

	public Insert value(final Object value) {
		onlyValues = onlyValues && true;
		return column(null).value(value);
	}

	public Insert values(final ValueHolder values) {
		for (final Entry<String, Object> entry : values)
			value(entry.getValue());
		return this;
	}

	public Insert defaults() {
		insertValues.clear();
		defaultValues = true;
		return this;
	}

	@Override
	public Query query() {
		return new Query(this);
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

		strings.add(options.pad(insertType.string(options)));

		strings.add(" ");
		strings.add(table);

		if (defaultValues) {
			strings.add("DEFAULT VALUES");
		} else {
			if (!onlyValues) {
				strings.add(" (");

				final StringJoiner columns = new StringJoiner();
				for (final InsertValue insertValue : insertValues)
					columns.add(QueryUtils.splitName(options, insertValue.getColumn())
							.string(options));
				strings.add(columns.toString(", "));

				strings.add(")");
			}

			strings.add(options.newLine());
			strings.add(options.pad("VALUES"));

			assert (!insertValues.isEmpty());

			final int count = insertValues.get(0)
					.getValues()
					.size();
			for (int i = 0; i < count; i++) {
				if (i > 0)
					strings.add(options.newLine())
							.add(options.pad(""));
				strings.add(" (");
				final StringJoiner values = new StringJoiner();
				for (final InsertValue insertValue : insertValues) {
					if (options.prepare()) {
						values.add("?");
						options.addPreparedValue(insertValue.getValues()
								.get(i));
					} else
						values.add(QueryUtils.valueToString(options, insertValue.getValues()
								.get(i)));
				}
				strings.add(values.toString(", "));
				strings.add(")");
			}
		}

		return strings.toString();
	}

}
