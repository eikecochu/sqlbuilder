package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import de.ec.sql.Keyword.SecondaryKeyword;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class GroupBy implements QueryBuilder, BeforeOrderBy, BeforeHaving, SecondaryKeyword {

	private BeforeGroupBy builder;
	private final List<String> columns = new ArrayList<>();

	protected GroupBy(final BeforeGroupBy builder) {
		this.builder = builder;
	}

	public GroupBy column(final String column) {
		columns.add(column);
		return this;
	}

	public GroupBy columns(final String... columns) {
		if (columns != null)
			for (final String column : columns)
				this.columns.add(column);
		return this;
	}

	@Override
	public String string() {
		return string(QueryOptions.DEFAULT_OPTIONS);
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (builder != null)
			strings.add(builder.string(options));

		if (!columns.isEmpty()) {
			strings.add(options.newLine());
			strings.add(options.padCased("GROUP BY"));

			final StringJoiner columnsStrings = new StringJoiner();
			for (final String column : columns)
				columnsStrings.add(QueryUtils.splitName(options, column)
						.string(options));
			strings.add(" ");
			strings.add(columnsStrings.toString(", "));
		}

		return strings.toString();
	}

}
