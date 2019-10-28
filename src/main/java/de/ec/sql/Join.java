package de.ec.sql;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class Join extends Conditionable<Join>
		implements QueryBuilder, BeforeJoin, BeforeWhere, BeforeGroupBy, BeforeOrderBy {

	public enum JoinMode implements QueryPart {
		INNER_JOIN("INNER JOIN"),
		OUTER_JOIN("OUTER JOIN"),
		LEFT_JOIN("LEFT JOIN"),
		RIGHT_JOIN("RIGHT JOIN"),
		CROSS_JOIN("CROSS JOIN");

		private final String string;

		private JoinMode(final String string) {
			this.string = string;
		}

		@Override
		public String string(final QueryOptions options) {
			return options.cased(string);
		}
	}

	private BeforeJoin builder;
	private final JoinMode joinMode;
	private Query query;
	private String name;
	private String sql;

	protected Join(final BeforeJoin builder) {
		this(builder, JoinMode.INNER_JOIN);
	}

	protected Join(final BeforeJoin builder, final JoinMode joinMode) {
		this.builder = builder;
		this.joinMode = joinMode;
	}

	public Join subquery(final Query query, final String name) {
		this.query = query;
		this.name = name;
		return this;
	}

	public Join table(final String name) {
		query = null;
		this.name = name;
		return this;
	}

	public Conditionable<Join> on() {
		return this;
	}

	@Override
	public String string(final QueryOptions options) {
		final StringJoiner strings = new StringJoiner();

		if (builder != null)
			strings.add(builder.string(options));

		if (sql != null) {
			strings.add(sql);
		} else {
			if (name != null) {
				strings.add(options.newLine());

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
		}

		return strings.toString();
	}

}
