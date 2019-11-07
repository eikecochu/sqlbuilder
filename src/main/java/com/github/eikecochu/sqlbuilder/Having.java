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
public class Having extends Conditionable<Having>
		implements QueryBuilder<Having>, BeforeOrderBy<Having>, BeforeUnion<Having> {

	private Conditionable<Having> condition;

	protected Having(final BeforeHaving<?> parent) {
		super(parent);
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
		else if (condition != null) {
			strings.add(options.padCased("HAVING"));
			strings.add(" ");
			strings.add(condition.string(options));
		}

		return strings.toString();
	}

}
