package de.ec.sql;

import de.ec.sql.Keyword.PrimaryKeyword;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Delete implements QueryBuilder, BeforeWhere, PrimaryKeyword {

	private BeforeDelete builder;
	private final String table;

	public Delete(final String table) {
		this.table = table;
	}

	public Delete(final Table table) {
		this(table.tableName());
	}

	protected Delete(final BeforeDelete builder, final String table) {
		this(table);
		this.builder = builder;
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (builder != null) {
			strings.add(builder.string(options));
			strings.add(options.newLine());
		}

		strings.add(options.padCased("DELETE FROM"));

		strings.add(" ");
		strings.add(table);

		return strings.toString();
	}

}
