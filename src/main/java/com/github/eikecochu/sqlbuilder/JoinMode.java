package com.github.eikecochu.sqlbuilder;

import lombok.ToString;

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