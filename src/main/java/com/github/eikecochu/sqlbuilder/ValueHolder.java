package com.github.eikecochu.sqlbuilder;

import java.util.Map.Entry;

/**
 * The ValueHolder instance allows to declare an arbitrary class as a
 * ValueHolder to be used in various methods of the SQLBuilder, for example
 * Where.where(ValueHolder). Instead of passing all values of an object
 * manually, this interface can be used to extract multiple values at once from
 * any object of classes that implement it.
 */
@FunctionalInterface
public interface ValueHolder extends Iterable<Entry<String, Object>> {
}
