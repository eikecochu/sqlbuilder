package com.github.eikecochu.sqlbuilder;

/**
 * The QueryProcessor interface to implement for query postprocessing. Instances
 * of this interface can be passed to a QueryBuilder instance to create the
 * query and then automatically pass it to the passed processor, which is then
 * returned as the method result instead of the query. This can be useful for
 * example if a method should not return a Query or QueryBuilder directly, but
 * another instance instead for fluent style.
 */
@FunctionalInterface
public interface QueryProcessor {

	/**
	 * The process method. Will receive the query builder.
	 * 
	 * @param <T>     The type of the passed builder
	 * @param builder The QueryBuilder to process
	 */
	<T extends QueryBuilder<T>> void process(T builder);

}