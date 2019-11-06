package com.github.eikecochu.sqlbuilder;

@FunctionalInterface
public interface QueryProcessor {

	void process(QueryBuilder builder);

}
