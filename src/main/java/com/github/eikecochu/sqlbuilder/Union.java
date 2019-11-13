package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * The UNION expression. Allows joining two SELECT statements to unify the
 * results. Columns must match.
 *
 * @author eike
 *
 */
@ToString
@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Union extends QueryPartImpl<Union> implements BeforeSelect<Union>, BeforeWith<Union> {

	private boolean all = false;

	protected Union(final BeforeUnion<?> parent) {
		super(parent);
	}

	/**
	 * Enable UNION ALL to join all results without removing duplicate results.
	 *
	 * @return This instance
	 */
	public Union all() {
		return all(true);
	}

	/**
	 * Enable UNION to join only distinct results. Removes duplicate results.
	 *
	 * @return This instance
	 */
	public Union distinct() {
		return all(false);
	}

	/**
	 * Pass true to enable UNION ALL or false to enable UNION.
	 *
	 * @param all True for ALL or false for DISTINCT
	 * @return This instance
	 */
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
