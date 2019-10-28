package de.ec.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Update implements QueryBuilder, BeforeWhere {

	protected enum UpdateType implements QueryPart {
		UPDATE("UPDATE"),
		UPDATE_OR_ROLLBACK("UPDATE OR ROLLBACK"),
		UPDATE_OR_ABORT("UPDATE OR ABORT"),
		UPDATE_OR_REPLACE("UPDATE OR REPLACE"),
		UPDATE_OR_FAIL("UPDATE OR FAIL"),
		UPDATE_OR_IGNORE("UPDATE OR IGNORE");

		private String string;

		private UpdateType(final String string) {
			this.string = string;
		}

		@Override
		public String string(final QueryOptions options) {
			return options.cased(string);
		}
	}

	private UpdateType updateType = UpdateType.UPDATE;
	private BeforeUpdate builder;
	private final String table;
	private final List<UpdateValue> updateValues = new ArrayList<>();
	private String sql;

	public Update(final String table) {
		this.table = table;
	}

	public Update(final Table table) {
		this(table.tableName());
	}

	protected Update(final BeforeUpdate builder, final String table) {
		this(table);
		this.builder = builder;
	}

	private Update setType(final UpdateType updateType) {
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

	public UpdateValue set(final String column) {
		final UpdateValue updateValue = new UpdateValue(this, column);
		updateValues.add(updateValue);
		return updateValue;
	}

	public Update set(final String column, final Object value) {
		return set(column).value(value);
	}

	public Update set(final ValueHolder values) {
		for (final Entry<String, Object> entry : values)
			set(entry.getKey(), entry.getValue());
		return this;
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (builder != null) {
			strings.add(builder.string(options));
			strings.add(options.newLine());
		}

		if (sql != null) {
			strings.add(sql);
		} else {
			strings.add(options.padCased(updateType.string(options)));

			strings.add(" ");
			strings.add(table);

			strings.add(options.newLine());
			strings.add(options.padCased("SET"));
			strings.add(" ");

			final StringJoiner sets = new StringJoiner();
			for (final UpdateValue updateValue : updateValues) {
				String column = QueryUtils.splitName(options, updateValue.getColumn())
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
		}

		return strings.toString();
	}

}
