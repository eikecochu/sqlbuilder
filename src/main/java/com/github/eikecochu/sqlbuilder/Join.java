package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Join extends Conditionable<Join> implements QueryBuilder<Join>, BeforeJoin<Join>, BeforeWhere<Join>,
		BeforeGroupBy<Join>, BeforeOrderBy<Join>, BeforeUnion<Join> {

	private final JoinMode joinMode;
	private QueryBuilder<?> subquery;
	private String name;

	protected Join(final BeforeJoin<?> parent) {
		this(parent, JoinMode.INNER_JOIN);
	}

	protected Join(final BeforeJoin<?> parent, final JoinMode joinMode) {
		super(parent);
		this.joinMode = joinMode;
	}

	/**
	 * Add a subquery as join target
	 *
	 * @param subquery The subquery to join to
	 * @param name     The name of the subquery
	 * @return This JOIN statement
	 */
	public Join subquery(final QueryBuilder<?> subquery, final String name) {
		this.subquery = subquery;
		this.name = name;
		return this;
	}

	/**
	 * Join to a table by name
	 *
	 * @param name The name of the table to join to
	 * @return This JOIN statement
	 */
	public Join table(final String name) {
		subquery = null;
		this.name = name;
		return this;
	}

	/**
	 * Set join conditions
	 *
	 * @return The ON statement
	 */
	public Conditionable<Join> on() {
		return this;
	}

	/**
	 * Set join conditions to join on two equal columns
	 *
	 * @param col1 The left column
	 * @param col2 The right column, equal to left
	 * @return This instance
	 */
	public Join onColsEq(final String col1, final String col2) {
		return on().colsEq(col1, col2);
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
		else if (name != null) {
			strings.add(options.padCased(joinMode.string(options)));

			if (subquery != null) {
				strings.add(" ");
				strings.add(subquery.string(options));
			}

			strings.add(" ");
			strings.add(QueryUtils.splitName(options, name)
					.string(options));

			final String condition = super.string(options);

			if (condition != null) {
				strings.add(" ");
				strings.add(options.cased("ON"));
				strings.add(" ");
				strings.add(condition);
			}
		}

		return strings.toString();
	}

}
