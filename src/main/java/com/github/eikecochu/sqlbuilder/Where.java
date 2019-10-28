package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Where extends Conditionable<Where> implements QueryBuilder, BeforeGroupBy, BeforeOrderBy {

	private BeforeWhere builder;
	private String sql;

	protected Where(final BeforeWhere builder) {
		this.builder = builder;
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (builder != null)
			strings.add(builder.string(options));

		if (sql != null) {
			strings.add(sql);
		} else {
			final String condition = super.string(options);

			if (condition != null && !condition.isEmpty()) {
				strings.add(options.newLine());
				strings.add(options.padCased("WHERE"));
				strings.add(" ");
				strings.add(condition);
			}
		}

		return strings.toString();
	}

}
