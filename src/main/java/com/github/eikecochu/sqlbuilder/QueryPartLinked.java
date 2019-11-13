package com.github.eikecochu.sqlbuilder;

/**
 * The QueryPartLinked interface is implemented by all expressions that can link
 * to parent expressions for building chains.
 *
 * @param <T> The type of the parent expression
 */
public interface QueryPartLinked<T extends QueryPartLinked<T>> extends QueryPart {

	/**
	 * Get the parent of this expression
	 *
	 * @return The parent expression
	 */
	QueryPartLinked<?> parent();

	/**
	 * Set the parent of this expression
	 *
	 * @param parent The parent expression
	 * @return This instance
	 */
	T parent(final QueryPartLinked<?> parent);

	/**
	 * Continue the building chain with any element, for example custom implemented
	 * expressions.
	 *
	 * @param     <U> The type of the parameter
	 * @param ext The custom expression
	 * @return The custom expression
	 */
	default <U extends QueryPartLinked<U>> U ext(final U ext) {
		if (ext == null)
			return null;
		return ext.parent(this);
	}

}
