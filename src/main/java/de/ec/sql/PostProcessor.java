package de.ec.sql;

import java.sql.Connection;

public interface PostProcessor<T> {

	T process(T sql, Connection connection);

}
