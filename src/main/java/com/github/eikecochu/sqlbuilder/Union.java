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
public class Union extends QueryPartImpl<Union> implements BeforeSelect<Union>, BeforeWith<Union> {

	private boolean all = false;

	protected Union(final BeforeUnion<?> parent) {
		super(parent);
	}

	public Union all() {
		return all(true);
	}

	public Union distinct() {
		return all(false);
	}

	public Union all(final boolean all) {
		this.all = all;
		return this;
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
			strings.add(options.padCased("UNION"));

			if (all) {
				strings.add(" ");
				strings.add(options.cased("ALL"));
			}
		}

		return strings.toString();
	}

}
