package com.github.eikecochu.sqlbuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Tests {

	public QueryOptions testOptions() {
		return new QueryOptions().pretty(false)
				.convert(Boolean.class, b -> b ? 1 : 0)
				.sqlPostprocessor((sql, connection) -> {
					System.out.println(sql);
					return sql;
				});
	}

	@Test
	public void testWith() {
		final String string = SQLBuilder.With("A")
				.as(SQLBuilder.Select("*")
						.from("TEST"))
				.select()
				.from("A")
				.string(testOptions());

		Assertions.assertEquals("WITH A AS (SELECT * FROM TEST) SELECT * FROM A", string);
	}

	@Test
	public void testSelect() {
		final String string = SQLBuilder.Select("COL1", "COL2")
				.from("TEST")
				.where()
				.col("COL1")
				.eq(1)
				.or()
				.col("COL2", true)
				.string(testOptions());

		Assertions.assertEquals("SELECT COL1, COL2 FROM TEST WHERE COL1 = 1 OR COL2 = 1", string);
	}

	@Test
	public void testExists() {
		final String string = SQLBuilder.Select("*")
				.from("TEST")
				.where()
				.col("A", 1)
				.and()
				.exists(SQLBuilder.Select("COL1")
						.from("TEST"))
				.string(testOptions());

		Assertions.assertEquals("SELECT * FROM TEST WHERE A = 1 AND EXISTS (SELECT COL1 FROM TEST)", string);
	}

	@Test
	public void testUnion() {
		final String string = SQLBuilder.Select("COL1")
				.from("TEST1")
				.union()
				.select("COL2")
				.from("TEST2")
				.string(testOptions());

		Assertions.assertEquals("SELECT COL1 FROM TEST1 UNION SELECT COL2 FROM TEST2", string);
	}

	@Test
	public void testBetween() {
		final String string = SQLBuilder.Select("COL1")
				.from("TEST")
				.where()
				.col("COL1")
				.between()
				.values(5, 10)
				.string(testOptions());

		Assertions.assertEquals("SELECT COL1 FROM TEST WHERE COL1 BETWEEN 5 AND 10", string);
	}

	@Test
	public void testAll() {
		final String string = SQLBuilder.Select("COL1")
				.from("TEST")
				.where()
				.col("COL1")
				.le()
				.any(SQLBuilder.Select("COL1")
						.from("TEST"))
				.string(testOptions());

		Assertions.assertEquals("SELECT COL1 FROM TEST WHERE COL1 <= ANY (SELECT COL1 FROM TEST)", string);
	}

	@Test
	public void testInsert() {
		final String string = SQLBuilder.Insert("TEST")
				.column("COL1", 1)
				.column("COL2")
				.value(true)
				.string(testOptions());

		Assertions.assertEquals("INSERT INTO TEST (COL1, COL2) VALUES (1, 1)", string);
	}

	@Test
	public void testUpdate() {
		final String string = SQLBuilder.Update("TEST")
				.set("COL1")
				.value(2)
				.set("COL2", true)
				.where()
				.col("COL1", 1)
				.string(testOptions());

		Assertions.assertEquals("UPDATE TEST SET COL1 = 2, COL2 = 1 WHERE COL1 = 1", string);
	}

	@Test
	public void testDelete() {
		final String string = SQLBuilder.Delete("TEST")
				.where()
				.col("COL1")
				.eq(1)
				.and()
				.col("COL2")
				.not()
				.eq(true)
				.string(testOptions());

		Assertions.assertEquals("DELETE FROM TEST WHERE COL1 = 1 AND NOT COL2 = 1", string);
	}

	@Test
	public void testNull() {
		final String string = SQLBuilder.Select("COL1")
				.from("TEST")
				.where()
				.col("COL1")
				.eq(null)
				.and()
				.col("COL2", 1)
				.string(testOptions());

		Assertions.assertEquals("SELECT COL1 FROM TEST WHERE COL2 = 1", string);
	}

	@Test
	public void testMiddleNull() {
		final String string = SQLBuilder.Select("COL1")
				.from("TEST")
				.where()
				.col("COL1", 1)
				.and()
				.col("COL2")
				.eq(null)
				.or()
				.col("COL3", 2)
				.string(testOptions());

		Assertions.assertEquals("SELECT COL1 FROM TEST WHERE COL1 = 1 OR COL3 = 2", string);
	}

}
