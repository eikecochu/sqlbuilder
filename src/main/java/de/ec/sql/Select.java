package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import de.ec.sql.Keyword.PrimaryKeyword;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Select implements QueryPart, BeforeFrom, PrimaryKeyword {

	private final List<String> columns = new ArrayList<>();
	private boolean distinct = false;
	private boolean all = false;
	private BeforeSelect builder;

	public Select(final String... columns) {
		columns(columns);
	}

	protected Select(final BeforeSelect builder) {
		this.builder = builder;
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

	public Select column(final String column) {
		columns.add(column);
		return this;
	}

	public Select columns(final String... columns) {
		if (columns != null)
			for (final String column : columns)
				this.columns.add(column);
		return this;
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (builder != null) {
			strings.add(builder.string(options));
			strings.add(options.newLine());
		}

		strings.add(options.padCased("SELECT"));

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
