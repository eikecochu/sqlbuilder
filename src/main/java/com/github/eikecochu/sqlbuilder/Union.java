package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Union extends SQLQueryPart<Union> implements QueryBuilder, BeforeSelect, BeforeWith {

	private boolean all = false;
	private BeforeUnion builder;

	Union(final BeforeUnion builder) {
		this.builder = builder;
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (builder != null) {
			strings.add(builder.string(options));
			strings.add(options.newLine());
		}

		if (sql() != null) {
			strings.add(sql());
		} else {
			strings.add(options.padCased("UNION"));

			if (all) {
				strings.add(" ");
				strings.add(options.cased("ALL"));
			}
		}

		return strings.toString();
	}

}
