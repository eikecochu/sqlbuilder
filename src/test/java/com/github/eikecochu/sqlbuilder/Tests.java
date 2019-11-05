package com.github.eikecochu.sqlbuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.eikecochu.sqlbuilder.oracle.ConnectBy;
import com.github.eikecochu.sqlbuilder.oracle.StartWith;

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
	public void testCol() {
		final Query query = SQLBuilder.Select("COL1", "COL2")
				.from("TEST")
				.where()
				.col("COL1")
				.eq()
				.col("COL2")
				.query();

		final String ugly = "SELECT COL1, COL2 FROM TEST WHERE COL1 = COL2";

		// @formatter:off
		final String pretty =
			"SELECT COL1, COL2" + NL +
			"  FROM TEST" + NL +
			" WHERE COL1 = COL2";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(pretty, query.string(testOptions().pretty(true)));
	}

	@Test
	public void testExpr() {
		final Query query = SQLBuilder.Select("COL1", "COL2")
				.from("TEST")
				.where()
				.col("COL1")
				.eq()
				.expr("UPPER(COL1)")
				.query();

		final String ugly = "SELECT COL1, COL2 FROM TEST WHERE COL1 = UPPER(COL1)";

		// @formatter:off
		final String pretty =
			"SELECT COL1, COL2" + NL +
			"  FROM TEST" + NL +
			" WHERE COL1 = UPPER(COL1)";
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

	@Test
	public void testIgnoreNull() {
		final Query query = SQLBuilder.Select("COL1")
				.from("TEST")
				.where()
				.col("A")
				.eq(null)
				.or()
				.col("B", 1)
				.query();

		final String ugly = "SELECT COL1 FROM TEST WHERE A IS NULL OR B = 1";

		// @formatter:off
		final String pretty =
			"SELECT COL1" + NL +
			"  FROM TEST" + NL +
			" WHERE A IS NULL OR B = 1";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions().ignoreNull(false)));
		Assertions.assertEquals(pretty, query.string(testOptions().ignoreNull(false)
				.pretty(true)));
	}

	@Test
	public void testDoubleWith() {
		final Query query = SQLBuilder.With("A")
				.as(SQLBuilder.Select()
						.from("TEST1"))
				.with("B")
				.as(SQLBuilder.Select()
						.from("TEST2"))
				.select()
				.from("A")
				.query();

		final String ugly = "WITH A AS (SELECT * FROM TEST1), B AS (SELECT * FROM TEST2) SELECT * FROM A";

		// @formatter:off
		final String pretty =
			"  WITH A AS (" + NL +
			"       SELECT *" + NL +
			"         FROM TEST1)," + NL +
			"       B AS (" + NL +
			"       SELECT *" + NL +
			"         FROM TEST2)" + NL +
			"SELECT *" + NL +
			"  FROM A";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions().ignoreNull(false)));
		Assertions.assertEquals(pretty, query.string(testOptions().ignoreNull(false)
				.pretty(true)));
	}

	@Test
	public void testDoubleWithSQL() {
		final Query query = SQLBuilder.With("A")
				.as(SQLBuilder.Select()
						.from("TEST1"))
				.withSQL("B AS (SELECT * FROM TEST2)")
				.select()
				.from("A")
				.query();

		final String ugly = "WITH A AS (SELECT * FROM TEST1), B AS (SELECT * FROM TEST2) SELECT * FROM A";

		// @formatter:off
		final String pretty =
			"  WITH A AS (" + NL +
			"       SELECT *" + NL +
			"         FROM TEST1)," + NL +
			"       B AS (SELECT * FROM TEST2)" + NL +
			"SELECT *" + NL +
			"  FROM A";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions().ignoreNull(false)));
		Assertions.assertEquals(pretty, query.string(testOptions().ignoreNull(false)
				.pretty(true)));
	}

	@Test
	public void testStartWith() {
		final Query query = SQLBuilder.Select()
				.from("TEST1")
				.ext(new StartWith())
				.col("A", 1)
				.connectBy()
				.col("A")
				.eqCol("PARENT")
				.query();

		final String ugly = "SELECT * FROM TEST1 START WITH A = 1 CONNECT BY PRIOR A = PARENT";

		// @formatter:off
		final String pretty =
			"SELECT *" + NL +
			"  FROM TEST1" + NL +
			" START WITH A = 1" + NL +
			"CONNECT BY PRIOR A = PARENT";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions().ignoreNull(false)));
		Assertions.assertEquals(pretty, query.string(testOptions().ignoreNull(false)
				.pretty(true)));
	}

	@Test
	public void testComplex() {
		final Query query = SQLBuilder.With("Recents")
				.columns("ITEM_ID", "ITEM_PUID", "TIME_CREATED")
				.as(SQLBuilder.Select()
						.distinct()
						.columns("ITEM_ID", "ITEM_PUID", "MAX(TIME_CREATED) TIME_CREATED")
						.from("RECENT_HISTORY")
						.where()
						.col("UPPER(USER_NAME)")
						.eq("JOE")
						.groupBy("ITEM_ID", "ITEM_PUID")
						.orderBy("TIME_CREATED DESC"))
				.select("rh.TYPE", "rh.ACTION", "rh.ITEM_ID", "rh.ITEM_PUID", "rh.USER_NAME", "rh.TIME_CREATED",
						"dt.SCRIPT_NAME", "kw.NAME AS KEYWORD_NAME", "cd.CLASS_NAME", "la.VALUE AS REQ_NAME",
						"dt.GRAPHICAL_REPORT_TYPE", "dt.IS_TRACE_REPORT", "dt.REPORT_TYPE",
						"dt.DESCRIPTION AS SCRIPT_DESCRIPTION", "kw.DESCRIPTION AS KEYWORD_DESCRIPTION",
						"ta.VALUE AS REQ_DESCRIPTION1", "ta.IS_HTML AS IS_HTML1", "ca.VALUE AS REQ_DESCRIPTION2",
						"ca.IS_HTML AS IS_HTML2")
				.from("RECENT_HISTORY rh")
				.leftJoin("DOCTOOL_SCRIPTS dt")
				.on()
				.col("rh.TYPE", 1)
				.col("rh.ITEM_ID")
				.eq()
				.col("dt.SCRIPT_ID")
				.leftJoin("KEYWORDS kw")
				.on()
				.col("rh.TYPE")
				.in(3, 4, 5, 6)
				.col("rh.ITEM_ID")
				.eq()
				.col("kw.KEYWORD_ID")
				.leftJoin("OBJECT1 o1")
				.on()
				.col("rh.TYPE", 2)
				.col("rh.ITEM_ID")
				.eq()
				.col("o1.CLASS_ID")
				.col("rh.ITEM_PUID")
				.eq()
				.col("o1.PUID")
				.col("o1.STATUS")
				.eq("Current")
				.leftJoin("CLASS_DEFINITION cd")
				.on()
				.group(new Condition().col("rh.TYPE", 2)
						.col("rh.ITEM_ID")
						.eq()
						.col("cd.CLASS_ID"))
				.or()
				.group(new Condition().col("rh.TYPE", 1)
						.col("dt.CLASS_ID")
						.eq()
						.col("cd.CLASS_ID"))
				.leftJoin("LINE_ATTRIBUTE1 la")
				.on()
				.col("rh.TYPE", 2)
				.col("la.ATTRIBUTE_ID")
				.eq()
				.col("cd.DEFAULT_TITLE")
				.col("la.OBJECT_ID")
				.eq()
				.col("o1.OBJECT_ID")
				.col("la.CLASS_ID")
				.eq()
				.col("rh.ITEM_ID")
				.leftJoin("TEXT_ATTRIBUTE1 ta")
				.on()
				.col("rh.TYPE", 2)
				.col("ta.ATTRIBUTE_ID")
				.eq()
				.col("cd.DEFAULT_DESCRIPTION")
				.col("ta.OBJECT_ID")
				.eq()
				.col("o1.OBJECT_ID")
				.col("ta.CLASS_ID")
				.eq()
				.col("rh.ITEM_ID")
				.leftJoin("CORE_TEXT_ATTRIBUTE1 ca")
				.on()
				.col("rh.TYPE", 2)
				.col("ca.ATTRIBUTE_ID")
				.eq()
				.col("cd.DEFAULT_DESCRIPTION")
				.col("ca.OBJECT_ID")
				.eq()
				.col("o1.OBJECT_ID")
				.col("ca.CLASS_ID")
				.eq()
				.col("rh.ITEM_ID")
				.innerJoin("Recents")
				.on()
				.col("rh.ITEM_ID")
				.eq()
				.col("Recents.ITEM_ID")
				.col("rh.TIME_CREATED")
				.eq()
				.col("Recents.TIME_CREATED")
				.and()
				.group(new Condition().col("rh.ITEM_PUID")
						.eq()
						.col("Recents.ITEM_PUID")
						.or()
						.group(new Condition().col("rh.ITEM_PUID")
								.isNull()
								.and()
								.col("Recents.ITEM_PUID")
								.isNull()))
				.where()
				.col("rh.TYPE")
				.in(1, 2, 3, 4, 5, 6)
				.col("UPPER(rh.USER_NAME)", "JOE")
				.orderBy("rh.TIME_CREATED DESC")
				.query();

		final String ugly = "WITH Recents (ITEM_ID, ITEM_PUID, TIME_CREATED) AS (SELECT DISTINCT ITEM_ID, ITEM_PUID, MAX(TIME_CREATED) "
				+ "TIME_CREATED FROM RECENT_HISTORY WHERE UPPER(USER_NAME) = 'JOE' GROUP BY ITEM_ID, ITEM_PUID ORDER BY TIME_CREATED DESC) "
				+ "SELECT RH.TYPE, RH.ACTION, RH.ITEM_ID, RH.ITEM_PUID, RH.USER_NAME, RH.TIME_CREATED, DT.SCRIPT_NAME, KW.NAME KEYWORD_NAME, "
				+ "CD.CLASS_NAME, LA.VALUE REQ_NAME, DT.GRAPHICAL_REPORT_TYPE, DT.IS_TRACE_REPORT, DT.REPORT_TYPE, DT.DESCRIPTION SCRIPT_DESCRIPTION, "
				+ "KW.DESCRIPTION KEYWORD_DESCRIPTION, TA.VALUE REQ_DESCRIPTION1, TA.IS_HTML IS_HTML1, CA.VALUE REQ_DESCRIPTION2, CA.IS_HTML IS_HTML2 "
				+ "FROM RECENT_HISTORY rh LEFT JOIN DOCTOOL_SCRIPTS dt ON RH.TYPE = 1 AND RH.ITEM_ID = DT.SCRIPT_ID LEFT JOIN KEYWORDS kw ON RH.TYPE "
				+ "IN (3, 4, 5, 6) AND RH.ITEM_ID = KW.KEYWORD_ID LEFT JOIN OBJECT1 o1 ON RH.TYPE = 2 AND RH.ITEM_ID = O1.CLASS_ID AND RH.ITEM_PUID = "
				+ "O1.PUID AND O1.STATUS = 'Current' LEFT JOIN CLASS_DEFINITION cd ON (RH.TYPE = 2 AND RH.ITEM_ID = CD.CLASS_ID) OR (RH.TYPE = 1 AND "
				+ "DT.CLASS_ID = CD.CLASS_ID) LEFT JOIN LINE_ATTRIBUTE1 la ON RH.TYPE = 2 AND LA.ATTRIBUTE_ID = CD.DEFAULT_TITLE AND LA.OBJECT_ID = "
				+ "O1.OBJECT_ID AND LA.CLASS_ID = RH.ITEM_ID LEFT JOIN TEXT_ATTRIBUTE1 ta ON RH.TYPE = 2 AND TA.ATTRIBUTE_ID = CD.DEFAULT_DESCRIPTION "
				+ "AND TA.OBJECT_ID = O1.OBJECT_ID AND TA.CLASS_ID = RH.ITEM_ID LEFT JOIN CORE_TEXT_ATTRIBUTE1 ca ON RH.TYPE = 2 AND CA.ATTRIBUTE_ID = "
				+ "CD.DEFAULT_DESCRIPTION AND CA.OBJECT_ID = O1.OBJECT_ID AND CA.CLASS_ID = RH.ITEM_ID INNER JOIN RECENTS ON RH.ITEM_ID = RECENTS.ITEM_ID "
				+ "AND RH.TIME_CREATED = RECENTS.TIME_CREATED AND (RH.ITEM_PUID = RECENTS.ITEM_PUID OR (RH.ITEM_PUID IS NULL AND RECENTS.ITEM_PUID IS "
				+ "NULL)) WHERE RH.TYPE IN (1, 2, 3, 4, 5, 6) AND UPPER(RH.USER_NAME) = 'JOE' ORDER BY RH.TIME_CREATED DESC";

		// @formatter:off
		final String pretty =
				"  WITH Recents (ITEM_ID, ITEM_PUID, TIME_CREATED) AS (" + NL +
				"       SELECT DISTINCT ITEM_ID, ITEM_PUID, MAX(TIME_CREATED) TIME_CREATED" + NL +
				"         FROM RECENT_HISTORY" + NL +
				"        WHERE UPPER(USER_NAME) = 'JOE'" + NL +
				"        GROUP BY ITEM_ID, ITEM_PUID" + NL +
				"        ORDER BY TIME_CREATED DESC)" + NL +
				"SELECT RH.TYPE, RH.ACTION, RH.ITEM_ID, RH.ITEM_PUID, RH.USER_NAME, RH.TIME_CREATED, DT.SCRIPT_NAME, KW.NAME KEYWORD_NAME, CD.CLASS_NAME, " +
				        "LA.VALUE REQ_NAME, DT.GRAPHICAL_REPORT_TYPE, DT.IS_TRACE_REPORT, DT.REPORT_TYPE, DT.DESCRIPTION SCRIPT_DESCRIPTION, KW.DESCRIPTION " +
				        "KEYWORD_DESCRIPTION, TA.VALUE REQ_DESCRIPTION1, TA.IS_HTML IS_HTML1, CA.VALUE REQ_DESCRIPTION2, CA.IS_HTML IS_HTML2" + NL +
				"  FROM RECENT_HISTORY rh" + NL +
				"  LEFT JOIN DOCTOOL_SCRIPTS dt ON RH.TYPE = 1 AND RH.ITEM_ID = DT.SCRIPT_ID" + NL +
				"  LEFT JOIN KEYWORDS kw ON RH.TYPE IN (3, 4, 5, 6) AND RH.ITEM_ID = KW.KEYWORD_ID" + NL +
				"  LEFT JOIN OBJECT1 o1 ON RH.TYPE = 2 AND RH.ITEM_ID = O1.CLASS_ID AND RH.ITEM_PUID = O1.PUID AND O1.STATUS = 'Current'" + NL +
				"  LEFT JOIN CLASS_DEFINITION cd ON (RH.TYPE = 2 AND RH.ITEM_ID = CD.CLASS_ID) OR (RH.TYPE = 1 AND DT.CLASS_ID = CD.CLASS_ID)" + NL +
				"  LEFT JOIN LINE_ATTRIBUTE1 la ON RH.TYPE = 2 AND LA.ATTRIBUTE_ID = CD.DEFAULT_TITLE AND LA.OBJECT_ID = O1.OBJECT_ID AND LA.CLASS_ID = RH.ITEM_ID" + NL +
				"  LEFT JOIN TEXT_ATTRIBUTE1 ta ON RH.TYPE = 2 AND TA.ATTRIBUTE_ID = CD.DEFAULT_DESCRIPTION AND TA.OBJECT_ID = O1.OBJECT_ID AND TA.CLASS_ID = RH.ITEM_ID" + NL +
				"  LEFT JOIN CORE_TEXT_ATTRIBUTE1 ca ON RH.TYPE = 2 AND CA.ATTRIBUTE_ID = CD.DEFAULT_DESCRIPTION AND CA.OBJECT_ID = O1.OBJECT_ID AND CA.CLASS_ID = RH.ITEM_ID" + NL +
				" INNER JOIN RECENTS ON RH.ITEM_ID = RECENTS.ITEM_ID AND RH.TIME_CREATED = RECENTS.TIME_CREATED AND (RH.ITEM_PUID = RECENTS.ITEM_PUID OR (RH.ITEM_PUID IS NULL AND RECENTS.ITEM_PUID IS NULL))" + NL +
				" WHERE RH.TYPE IN (1, 2, 3, 4, 5, 6) AND UPPER(RH.USER_NAME) = 'JOE'" + NL +
				" ORDER BY RH.TIME_CREATED DESC";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(pretty, query.string(testOptions().pretty(true)));
	}

	@Test
	public void testOracleHierarchyUp() {
		final Query query = SQLBuilder.Select()
				.from("CATEGORY")
				.ext(new ConnectBy())
				.col("CATEGORY_ID")
				.eqCol("PARENT_ID")
				.startWith()
				.col("CATEGORY_ID", 15)
				.query();

		final String ugly = "SELECT * FROM CATEGORY CONNECT BY PRIOR CATEGORY_ID = PARENT_ID START WITH CATEGORY_ID = 15";

		// @formatter:off
		final String pretty =
			"SELECT *" + NL +
			"  FROM CATEGORY" + NL +
			"CONNECT BY PRIOR CATEGORY_ID = PARENT_ID" + NL +
			" START WITH CATEGORY_ID = 15";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(pretty, query.string(testOptions().pretty(true)));
	}

	@Test
	public void testWithHierarchyUp() {
		final Query query = SQLBuilder.With("CTE")
				.as(SQLBuilder.Select()
						.from("CATEGORY")
						.where()
						.col("CATEGORY_ID", 15)
						.unionAll()
						.select()
						.from("CATEGORY C1")
						.innerJoin("CTE")
						.on()
						.col("CTE.CATEGORY_ID")
						.eqCol("C1.PARENT_ID"))
				.select()
				.from("CTE")
				.query();

		final String ugly = "WITH CTE AS (SELECT * FROM CATEGORY WHERE CATEGORY_ID = 15 UNION ALL SELECT * FROM CATEGORY C1 INNER JOIN CTE ON CTE.CATEGORY_ID = C1.PARENT_ID) SELECT * FROM CTE";

		// @formatter:off
		final String pretty =
			"  WITH CTE AS (" + NL +
			"       SELECT *" + NL +
			"         FROM CATEGORY" + NL+
			"        WHERE CATEGORY_ID = 15" + NL+
			"        UNION ALL" + NL+
			"       SELECT *" + NL +
			"         FROM CATEGORY C1" + NL+
			"        INNER JOIN CTE ON CTE.CATEGORY_ID = C1.PARENT_ID)" + NL+
			"SELECT *" +NL+
			"  FROM CTE";
		// @formatter:on

		Assertions.assertEquals(ugly, query.string(testOptions()));
		Assertions.assertEquals(pretty, query.string(testOptions().pretty(true)));
	}

	@Test
	public void testExpression() {
		final Expression e = new Expression("PACKAGE.PROCEDURE", 1, 2, 3, null);
		final String actual = e.valueString(testOptions());

		final String expected = "PACKAGE.PROCEDURE(1, 2, 3)";

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void testExpressionPrepare() {
		final Expression e = new Expression("PACKAGE.PROCEDURE", 1, 2, 3, null);
		final String actual = e.string(testOptions().prepare(true));

		final String expected = "PACKAGE.PROCEDURE(?, ?, ?)";

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void testProcessor() {
		class Processor implements QueryProcessor {
			private Query query;

			@Override
			public void process(Query query) {
				this.query = query;
			}

			public String convertToString() {
				return query.string(testOptions());
			}
		}

		final String actual = SQLBuilder.Select()
				.from("TEST1")
				.query(new Processor())
				.convertToString();

		final String expected = "SELECT * FROM TEST1";

		Assertions.assertEquals(expected, actual);
	}

}
