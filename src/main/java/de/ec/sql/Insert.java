package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class Insert implements QueryBuilder, QueryPart {

	private static enum InsertType implements QueryPart {
		INSERT("INSERT INTO"),
		REPLACE("REPLACE INTO"),
		INSERT_OR_REPLACE("INSERT OR REPLACE INTO"),
		INSERT_OR_ROLLBACK("INSERT OR ROLLBACK INTO"),
		INSERT_OR_ABORT("INSERT OR ABORT INTO"),
		INSERT_OR_FAIL("INSERT OR FAIL INTO"),
		INSERT_OR_IGNORE("INSERT OR IGNORE INTO");

		private String string;

		private InsertType(String string) {
			this.string = string;
		}

		@Override
		public String string() {
			return string(QueryOptions.DEFAULT_OPTIONS);
		}

		@Override
		public String string(QueryOptions options) {
			return options.cased(string);
		}
	}

	private With with;
	private String table;
	private InsertType insertType = InsertType.INSERT;
	private final List<InsertValue> insertValues = new ArrayList<>();
	private boolean defaultValues = false;
	private boolean onlyValues = true;

	public Insert(String table) {
		this.table = table;
	}

	public Insert(Table table) {
		this(table.tableName());
	}

	protected Insert(With with, String table) {
		this(table);
		this.with = with;
	}

	protected Insert setType(InsertType type) {
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

	public Select select() {
		insertValues.clear();
		defaultValues = false;
		return new Select(this);
	}

	public InsertValue column(String column) {
		defaultValues = false;
		onlyValues = false;
		InsertValue insertValue = new InsertValue(this, column);
		insertValues.add(insertValue);
		return insertValue;
	}

	public Insert column(String column, Object value) {
		return column(column).value(value);
	}

	public Insert value(Object value) {
		onlyValues = onlyValues && true;
		return column(null).value(value);
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
	public String string(QueryOptions options) {
		StringJoiner strings = new StringJoiner();

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

				StringJoiner columns = new StringJoiner();
				for (InsertValue insertValue : insertValues)
					columns.add(QueryUtils.splitName(insertValue.getColumn())
						.string(options));
				strings.add(columns.toString(", "));

				strings.add(")");
			}

			strings.add(options.newLine());
			strings.add(options.pad("VALUES"));

			assert (!insertValues.isEmpty());

			int count = insertValues.get(0)
				.getValues()
				.size();
			for (int i = 0; i < count; i++) {
				if (i > 0)
					strings.add(options.newLine())
						.add(options.pad(""));
				strings.add(" (");
				StringJoiner values = new StringJoiner();
				for (InsertValue insertValue : insertValues) {
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
