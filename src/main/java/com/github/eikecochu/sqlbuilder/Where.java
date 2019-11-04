package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Where extends Conditionable<Where> implements QueryBuilder, BeforeGroupBy, BeforeOrderBy, BeforeUnion {

	protected Where(final BeforeWhere parent) {
		super(parent);
	}

	public Where exists(final QueryBuilder query) {
		return addPart(new Exists(query));
	}

	public Where exists(final String sql) {
		return addPart(new Exists(SQLBuilder.Query(sql)));
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (parent() != null)
			strings.add(parent().string(options));

		if (strings.notEmpty())
			strings.add(options.newLine());

		if (sql() != null)
			strings.add(options.padded(sql()));
		else {
			final String condition = super.string(options);

			if (condition != null && !condition.isEmpty()) {
				strings.add(options.padCased("WHERE"));
				strings.add(" ");
				strings.add(condition);
			}
		}

		return strings.toString();
	}

}
