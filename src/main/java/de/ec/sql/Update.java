package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

public class Update implements QueryBuilder, QueryPart, Whereable {

	private static enum UpdateType implements QueryPart {
		UPDATE("UPDATE"), UPDATE_OR_ROLLBACK("UPDATE OR ROLLBACK"), UPDATE_OR_ABORT(
				"UPDATE OR ABORT"), UPDATE_OR_REPLACE(
						"UPDATE OR REPLACE"), UPDATE_OR_FAIL("UPDATE OR FAIL"), UPDATE_OR_IGNORE("UPDATE OR IGNORE");

		private String string;

		private UpdateType(String string) {
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

	private UpdateType updateType = UpdateType.UPDATE;
	private With with;
	private final String table;
	private final List<UpdateValue> updateValues = new ArrayList<>();

	public Update(String table) {
		this.table = table;
	}
	
	public Update(Table table) {
		this(table.tableName());
	}

	protected Update(With with, String table) {
		this(table);
		this.with = with;
	}

	private Update setType(UpdateType updateType) {
		this.updateType = updateType;
		return this;
	}

	public Update orRollback() {
		return setType(UpdateType.UPDATE_OR_ROLLBACK);
	}

	public Update orAbort() {
		return setType(UpdateType.UPDATE_OR_ABORT);
	}

	public Update orFail() {
		return setType(UpdateType.UPDATE_OR_FAIL);
	}

	public Update orReplace() {
		return setType(UpdateType.UPDATE_OR_REPLACE);
	}

	public Update orIgnore() {
		return setType(UpdateType.UPDATE_OR_IGNORE);
	}

	public UpdateValue set(String column) {
		UpdateValue updateValue = new UpdateValue(this, column);
		updateValues.add(updateValue);
		return updateValue;
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

		strings.add(options.pad(updateType.string(options)));

		strings.add(" ");
		strings.add(table);

		strings.add(options.newLine());
		strings.add(options.pad("SET"));
		strings.add(" ");

		StringJoiner sets = new StringJoiner();
		for (UpdateValue updateValue : updateValues) {
			String column = QueryUtils.splitName(updateValue.getColumn())
				.string(options) + " = ";
			if (updateValue.isExpression())
				column += updateValue.getValue()
					.toString();
			else if (options.prepare()) {
				column += "?";
				options.addPreparedValue(updateValue.getValue());
			} else
				column += QueryUtils.valueToString(options, updateValue.getValue());
			sets.add(column);
		}
		strings.add(sets.toString(", "));

		return strings.toString();
	}

}
