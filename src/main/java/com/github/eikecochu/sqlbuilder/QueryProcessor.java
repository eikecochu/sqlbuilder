package com.github.eikecochu.sqlbuilder;

@FunctionalInterface
public interface QueryProcessor {

	<T extends QueryBuilder<T>> void process(T builder);

}
