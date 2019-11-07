package com.github.eikecochu.sqlbuilder.oracle;

import com.github.eikecochu.sqlbuilder.Conditionable;
import com.github.eikecochu.sqlbuilder.QueryBuilder;
import com.github.eikecochu.sqlbuilder.QueryOptions;
import com.github.eikecochu.sqlbuilder.QueryPartLinked;
import com.github.eikecochu.sqlbuilder.StringJoiner;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@NoArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class ConnectBy extends Conditionable<ConnectBy> implements QueryBuilder<ConnectBy> {

	private boolean nocycle;

	protected ConnectBy(final QueryPartLinked<?> parent) {
		super(parent);
	}

	public ConnectBy connectBy() {
		return new ConnectBy().parent(this);
	}

	public ConnectBy cycle() {
		nocycle = false;
		return this;
	}

	public ConnectBy nocycle() {
		nocycle = true;
		return this;
	}

	public StartWith startWith() {
		return new StartWith(this);
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
			final String condition = super.string(options);

			if (condition != null && !condition.isEmpty()) {
				strings.add(options.padCased("CONNECT BY"));
				if (nocycle)
					strings.add(" NOCYCLE");
				strings.add(" PRIOR");
				strings.add(" ");
				strings.add(condition);
			}
		}

		return strings.toString();
	}

}
