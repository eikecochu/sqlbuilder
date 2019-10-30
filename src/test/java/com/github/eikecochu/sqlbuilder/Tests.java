package com.github.eikecochu.sqlbuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Tests {

	private static final String NL = "\n";

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
		final Query query = SQLBuilder.With("A")
				.as(SQLBuilder.Select("*")
						.from("TEST"))
				.select()
				.from("A")
				.query();

		final String ugly = "WITH A AS (SELECT * FROM TEST) SELECT * FROM A";

		// @formatter:off
		final String pretty =
			"  WITH A AS (" + NL +
			"       SELECT *" + NL +
			"         FROM TEST)" + NL +
			"SELECT *" + NL +
			"  FROM A";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(pretty, query.string(testOptions().pretty(true)));
	}

	@Test
	public void testSelect() {
		final Query query = SQLBuilder.Select("COL1", "COL2")
				.from("TEST")
				.where()
				.col("COL1")
				.eq(1)
				.or()
				.col("COL2", true)
				.query();

		final String ugly = "SELECT COL1, COL2 FROM TEST WHERE COL1 = 1 OR COL2 = 1";

		// @formatter:off
		final String pretty =
			"SELECT COL1, COL2" + NL +
			"  FROM TEST" + NL +
			" WHERE COL1 = 1 OR COL2 = 1";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(pretty, query.string(testOptions().pretty(true)));
	}

	@Test
	public void testExists() {
		final Query query = SQLBuilder.Select("*")
				.from("TEST")
				.where()
				.col("A", 1)
				.and()
				.exists(SQLBuilder.Select("COL1")
						.from("TEST"))
				.query();

		final String ugly = "SELECT * FROM TEST WHERE A = 1 AND EXISTS (SELECT COL1 FROM TEST)";

		// @formatter:off
		final String pretty =
			"SELECT *" + NL +
			"  FROM TEST" + NL +
			" WHERE A = 1 AND EXISTS (" + NL +
			"       SELECT COL1" + NL +
			"         FROM TEST)";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(pretty, query.string(testOptions().pretty(true)));
	}

	@Test
	public void testUnion() {
		final Query query = SQLBuilder.Select("COL1")
				.from("TEST1")
				.union()
				.select("COL2")
				.from("TEST2")
				.query();

		final String ugly = "SELECT COL1 FROM TEST1 UNION SELECT COL2 FROM TEST2";

		// @formatter:off
		final String pretty =
			"SELECT COL1" + NL +
			"  FROM TEST1" + NL +
			" UNION" + NL +
			"SELECT COL2" + NL +
			"  FROM TEST2";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(pretty, query.string(testOptions().pretty(true)));
	}

	@Test
	public void testNested1() {
		final Query query = SQLBuilder.Select("COL1")
				.from("TEST")
				.where()
				.groupStart()
				.col("A", 1)
				.or()
				.col("B", 2)
				.groupEnd()
				.and()
				.col("C", 3)
				.query();

		final String ugly = "SELECT COL1 FROM TEST WHERE (A = 1 OR B = 2) AND C = 3";

		// @formatter:off
		final String pretty = "SELECT COL1" + NL +
						"  FROM TEST" + NL +
						" WHERE (A = 1 OR B = 2) AND C = 3";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(pretty, query.string(testOptions().pretty(true)));
	}

	@Test
	public void testNested2() {
		final Query query = SQLBuilder.Select("COL1")
				.from("TEST")
				.where()
				.group(new Condition().col("A", 1)
						.or()
						.col("B", 2))
				.and()
				.col("C", 3)
				.query();

		final String ugly = "SELECT COL1 FROM TEST WHERE (A = 1 OR B = 2) AND C = 3";

		// @formatter:off
		final String pretty =
			"SELECT COL1" + NL +
			"  FROM TEST" + NL +
			" WHERE (A = 1 OR B = 2) AND C = 3";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(pretty, query.string(testOptions().pretty(true)));
	}

	@Test
	public void testDoubleNegate() {
		final Query query = SQLBuilder.Select("COL1")
				.from("TEST")
				.where()
				.col("COL1")
				.not()
				.not()
				.eq(1)
				.query();

		final String ugly = "SELECT COL1 FROM TEST WHERE COL1 = 1";

		// @formatter:off
		final String pretty = "SELECT COL1" + NL +
						"  FROM TEST" + NL +
						" WHERE COL1 = 1";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(pretty, query.string(testOptions().pretty(true)));
	}

	@Test
	public void testBetween() {
		final Query query = SQLBuilder.Select("COL1")
				.from("TEST")
				.where()
				.col("COL1")
				.between()
				.values(5, 10)
				.query();

		final String ugly = "SELECT COL1 FROM TEST WHERE COL1 BETWEEN 5 AND 10";

		// @formatter:off
		final String pretty =
			"SELECT COL1" + NL +
			"  FROM TEST" + NL +
			" WHERE COL1 BETWEEN 5 AND 10";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(pretty, query.string(testOptions().pretty(true)));
	}

	@Test
	public void testAll() {
		final Query query = SQLBuilder.Select("COL1")
				.from("TEST")
				.where()
				.col("COL1")
				.le()
				.any(SQLBuilder.Select("COL1")
						.from("TEST"))
				.query();

		final String ugly = "SELECT COL1 FROM TEST WHERE COL1 <= ANY (SELECT COL1 FROM TEST)";

		// @formatter:off
		final String pretty =
			"SELECT COL1" + NL +
			"  FROM TEST" + NL +
			" WHERE COL1 <= ANY (" + NL +
			"       SELECT COL1" + NL +
			"         FROM TEST)";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(pretty, query.string(testOptions().pretty(true)));
	}

	@Test
	public void testInsert() {
		final Query query = SQLBuilder.Insert("TEST")
				.column("COL1", 1)
				.column("COL2")
				.value(true)
				.query();

		final String ugly = "INSERT INTO TEST (COL1, COL2) VALUES (1, 1)";

		// @formatter:off
		final String pretty = "INSERT INTO TEST (COL1, COL2)" + NL +
						"VALUES (1, 1)";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(pretty, query.string(testOptions().pretty(true)));
	}

	@Test
	public void testUpdate() {
		final Query query = SQLBuilder.Update("TEST")
				.set("COL1")
				.value(2)
				.set("COL2", true)
				.where()
				.col("COL1", 1)
				.query();

		final String ugly = "UPDATE TEST SET COL1 = 2, COL2 = 1 WHERE COL1 = 1";

		// @formatter:off
		final String pretty =
			"UPDATE TEST" + NL +
			"   SET COL1 = 2, COL2 = 1" + NL +
			" WHERE COL1 = 1";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(pretty, query.string(testOptions().pretty(true)));
	}

	@Test
	public void testDelete() {
		final Query query = SQLBuilder.Delete("TEST")
				.where()
				.col("COL1")
				.eq(1)
				.and()
				.col("COL2")
				.not()
				.eq(true)
				.query();

		final String ugly = "DELETE FROM TEST WHERE COL1 = 1 AND NOT COL2 = 1";

		// @formatter:off
		final String pretty =
			"DELETE FROM TEST" + NL +
			" WHERE COL1 = 1 AND NOT COL2 = 1";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(pretty, query.string(testOptions().pretty(true)));
	}

	@Test
	public void testNull() {
		final Query query = SQLBuilder.Select("COL1")
				.from("TEST")
				.where()
				.col("COL1")
				.eq(null)
				.and()
				.col("COL2", 1)
				.query();

		final String ugly = "SELECT COL1 FROM TEST WHERE COL2 = 1";

		// @formatter:off
		final String pretty =
			"SELECT COL1" + NL +
			"  FROM TEST" + NL +
			" WHERE COL2 = 1";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(pretty, query.string(testOptions().pretty(true)));
	}

	@Test
	public void testMiddleNull() {
		final Query query = SQLBuilder.Select("COL1")
				.from("TEST")
				.where()
				.col("COL1", 1)
				.and()
				.col("COL2")
				.eq(null)
				.or()
				.col("COL3", 2)
				.query();

		final String ugly = "SELECT COL1 FROM TEST WHERE COL1 = 1 OR COL3 = 2";

		// @formatter:off
		final String pretty =
			"SELECT COL1" + NL +
			"  FROM TEST" + NL +
			" WHERE COL1 = 1 OR COL3 = 2";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(pretty, query.string(testOptions().pretty(true)));
	}

	@Test
	public void testSQLParts() {
		final Query query = SQLBuilder.Select("COL1")
				.fromSQL("FROM TEST")
				.whereSQL("WHERE COL1 = 1")
				.query();

		final String ugly = "SELECT COL1 FROM TEST WHERE COL1 = 1";

		// @formatter:off
		final String pretty =
			"SELECT COL1" + NL +
			"  FROM TEST" + NL +
			" WHERE COL1 = 1";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(pretty, query.string(testOptions().pretty(true)));
	}

	@Test
	public void testSQL() {
		final Query query = SQLBuilder.Query("SELECT COL1 FROM TEST");

		final String ugly = "SELECT COL1 FROM TEST";

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(ugly, query.string(testOptions().pretty(true)));
	}

}
