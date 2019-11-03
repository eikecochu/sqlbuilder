package com.github.eikecochu.sqlbuilder;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Join extends Conditionable<Join>
		implements QueryBuilder, BeforeJoin, BeforeWhere, BeforeGroupBy, BeforeOrderBy, BeforeUnion {

	@ToString
	public enum JoinMode implements QueryPart {
		INNER_JOIN("INNER JOIN"),
		OUTER_JOIN("OUTER JOIN"),
		LEFT_JOIN("LEFT JOIN"),
		RIGHT_JOIN("RIGHT JOIN"),
		CROSS_JOIN("CROSS JOIN");

		private final String string;

		JoinMode(final String string) {
			this.string = string;
		}

		@Override
		public String string(final QueryOptions options) {
			return options.cased(string);
		}
	}

	private final JoinMode joinMode;
	private Query query;
	private String name;

	protected Join(final BeforeJoin parent) {
		this(parent, JoinMode.INNER_JOIN);
	}

	protected Join(final BeforeJoin parent, final JoinMode joinMode) {
		super(parent);
		this.joinMode = joinMode;
	}

	/**
	 * Add a subquery as join target
	 *
	 * @param query The subquery to join to
	 * @param name  The name of the subquery
	 * @return This JOIN statement
	 */
	public Join subquery(final Query query, final String name) {
		this.query = query;
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
		query = null;
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

			if (query != null) {
				strings.add(" ");
				strings.add(query.string(options));
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
