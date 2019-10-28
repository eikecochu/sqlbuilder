package de.ec.sql;

import de.ec.sql.Keyword.SecondaryKeyword;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Where extends Conditionable<Where>
		implements QueryBuilder, BeforeGroupBy, BeforeOrderBy, SecondaryKeyword {

	private BeforeWhere builder;

	protected Where(final BeforeWhere builder) {
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

		final String condition = super.string(options);

		if (condition != null && !condition.isEmpty()) {
			strings.add(options.newLine());
			strings.add(options.padCased("WHERE"));
			strings.add(" ");
			strings.add(condition);
		}

		return strings.toString();
	}

}
