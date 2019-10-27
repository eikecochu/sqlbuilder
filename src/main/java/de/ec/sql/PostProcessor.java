package de.ec.sql;

public interface PostProcessor<T> {

	T process(T sql);

}
