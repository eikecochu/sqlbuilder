package de.ec.sql;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
class Name implements QueryPart {

	private String function;
	private String schema;
	private String name;
	private String alias;

	@Override
	public String string() {
		return string(QueryOptions.DEFAULT_OPTIONS);
	}

	@Override
	public String string(QueryOptions options) {
		StringBuilder sb = new StringBuilder();

		if (function != null)
			sb.append(options.cased(function))
				.append("(");

		if (schema != null)
			sb.append(schema)
				.append(".");

		if (name.equals("*"))
			sb.append(name);
		else
			sb.append(options.cased(name));

		if (function != null)
			sb.append(")");

		if (alias != null)
			sb.append(" ")
				.append(alias);

		return sb.toString();
	}

}
