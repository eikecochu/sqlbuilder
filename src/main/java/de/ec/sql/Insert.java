package de.ec.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import de.ec.sql.Keyword.PrimaryKeyword;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Insert implements QueryBuilder, BeforeSelect, PrimaryKeyword {

	private enum InsertType implements QueryPart {
		INSERT("INSERT INTO"), REPLACE("REPLACE INTO"), INSERT_OR_REPLACE("INSERT OR REPLACE INTO"),
		INSERT_OR_ROLLBACK("INSERT OR ROLLBACK INTO"), INSERT_OR_ABORT("INSERT OR ABORT INTO"),
		INSERT_OR_FAIL("INSERT OR FAIL INTO"), INSERT_OR_IGNORE("INSERT OR IGNORE INTO");

		private String string;

		private InsertType(final String string) {
			this.string = string;
		}

		@Override
		public String string(final QueryOptions options) {
			return options.cased(string);
		}
	}

	private BeforeInsert builder;
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

	protected Insert(final BeforeInsert builder, final String table) {
		this(table);
		this.builder = builder;
	}

	public Insert replace() {
		return insertType(InsertType.REPLACE);
	}

	public Insert orReplace() {
		return insertType(InsertType.INSERT_OR_REPLACE);
	}

	public Insert orRollback() {
		return insertType(InsertType.INSERT_OR_ROLLBACK);
	}

	public Insert orAbort() {
		return insertType(InsertType.INSERT_OR_ABORT);
	}

	public Insert orFail() {
		return insertType(InsertType.INSERT_OR_FAIL);
	}

	public Insert orIgnore() {
		return insertType(InsertType.INSERT_OR_IGNORE);
	}

	@Override
	public Select select() {
		insertValues.clear();
		defaultValues = false;
		return BeforeSelect.super.select();
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
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (builder != null) {
			strings.add(builder.string(options));
			strings.add(options.newLine());
		}

		strings.add(options.padCased(insertType.string(options)));

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
			strings.add(options.padCased("VALUES"));

			assert (!insertValues.isEmpty());

			final int count = insertValues.get(0)
					.getValues()
					.size();
			for (int i = 0; i < count; i++) {
				if (i > 0)
					strings.add(options.newLine())
							.add(options.padCased(""));
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
