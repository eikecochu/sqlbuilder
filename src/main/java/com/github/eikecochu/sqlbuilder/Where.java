package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Where extends Conditionable<Where> implements QueryBuilder, BeforeGroupBy, BeforeOrderBy, BeforeUnion {

	private BeforeWhere builder;

	protected Where(final BeforeWhere builder) {
		this.builder = builder;
	}

	public Where exists(final QueryBuilder builder) {
		return addPart(new Exists(builder));
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (builder != null)
			strings.add(builder.string(options));

		if (sql() != null) {
			if (!strings.isEmpty())
				strings.add(options.newLine());
			strings.add(options.padded(sql()));
		} else {
			final String condition = super.string(options);

			if (condition != null && !condition.isEmpty()) {
				if (!strings.isEmpty())
					strings.add(options.newLine());
				strings.add(options.padCased("WHERE"));
				strings.add(" ");
				strings.add(condition);
			}
		}

		return strings.toString();
	}

}
