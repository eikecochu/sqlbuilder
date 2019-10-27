package de.ec.sql;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Tests {

	public QueryOptions testOptions() {
		return new QueryOptions().pretty(false)
				.backticks(false);
	}

	@Test
	public void testSelect() {
		final String string = new Select("COL1", "COL2").from("TEST")
				.where()
				.col("COL1")
				.eq(1)
				.or()
				.col("COL2", true)
				.query()
				.string(testOptions());

		Assertions.assertEquals("SELECT COL1, COL2 FROM TEST WHERE COL1 = 1 OR COL2 = true", string);
	}

	@Test
	public void testInsert() {
		final String string = new Insert("TEST").column("COL1", 1)
				.column("COL2")
				.value(true)
				.query()
				.string(testOptions());

		Assertions.assertEquals("INSERT INTO TEST (COL1, COL2) VALUES (1, true)", string);
	}

	@Test
	public void testUpdate() {
		final String string = new Update("TEST").set("COL1")
				.value(2)
				.set("COL2", true)
				.where()
				.col("COL1", 1)
				.string(testOptions());

		Assertions.assertEquals("UPDATE TEST SET COL1 = 2, COL2 = true WHERE COL1 = 1", string);
	}

	@Test
	public void testDelete() {
		final String string = new Delete("TEST").where()
				.col("COL1")
				.eq(1)
				.and()
				.col("COL2")
				.not()
				.eq(true)
				.query()
				.string(testOptions());

		Assertions.assertEquals("DELETE FROM TEST WHERE COL1 = 1 AND NOT COL2 = true", string);
	}

}
