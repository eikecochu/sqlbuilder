package com.github.eikecochu.sqlbuilder;

public interface BeforeUnion extends QueryPart {

	default Union union() {
		return new Union(this);
	}

	default Union unionAll() {
		return union().all(true);
	}

}
