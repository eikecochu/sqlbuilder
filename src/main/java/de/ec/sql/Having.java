package de.ec.sql;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Having extends Conditionable<Having> implements QueryBuilder, BeforeOrderBy {

	private BeforeHaving builder;
	private Conditionable<Having> condition;
	private String sql;

	protected Having(final BeforeHaving builder) {
		this.builder = builder;
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

		if (sql != null) {
			strings.add(sql);
		} else {
			if (condition != null) {
				strings.add(options.newLine());
				strings.add(options.padCased("HAVING"));
				strings.add(" ");
				strings.add(condition.string(options));
			}
		}

		return strings.toString();
	}

}
