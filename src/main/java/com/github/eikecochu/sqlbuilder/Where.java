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

	public Where exists(final QueryBuilder parent) {
		return addPart(new Exists(parent));
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (parent() != null)
			strings.add(parent().string(options));

		if (sql() != null) {
			if (strings.notEmpty())
				strings.add(options.newLine());
			strings.add(options.padded(sql()));
		} else {
			final String condition = super.string(options);

			if (condition != null && !condition.isEmpty()) {
				if (strings.notEmpty())
					strings.add(options.newLine());
				strings.add(options.padCased("WHERE"));
				strings.add(" ");
				strings.add(condition);
			}
		}

		return strings.toString();
	}

}
