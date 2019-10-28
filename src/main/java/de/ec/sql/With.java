package de.ec.sql;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class With
		implements QueryPart, BeforeWith, BeforeSelect, BeforeUpdate, BeforeDelete, BeforeInsert {

	private BeforeWith builder;
	private final String name;
	private final List<String> columns = new ArrayList<>();
	private Query query;
	private boolean recursive;
	private String sql;

	public With(final String name) {
		this.name = name;
	}

	protected With(final BeforeWith builder, final String name) {
		this.name = name;
		this.builder = builder;
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

	public With as(final QueryBuilder builder) {
		return as(builder.query());
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (builder == null) {
			if (sql == null) {
				strings.add(options.padCased("WITH"));

				if (recursive) {
					strings.add(" ");
					strings.add(options.cased("RECURSIVE"));
				}
			}
		} else {
			strings.add(options.newLine());
			strings.add(builder.string(options));
			strings.add(",");
		}

		if (sql != null) {
			strings.add(sql);
		} else {
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
		}

		return strings.toString();
	}

}
