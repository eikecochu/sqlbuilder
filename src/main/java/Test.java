import de.ec.sql.Condition;
import de.ec.sql.Delete;
import de.ec.sql.Insert;
import de.ec.sql.QueryOptions;
import de.ec.sql.Select;
import de.ec.sql.Update;
import de.ec.sql.With;

public class Test {

	public static void main(String[] args) {

		System.out.println(new With("Recents").columns("ITEM_ID", "ITEM_PUID", "TIME_CREATED")
				.as(new Select().distinct().columns("ITEM_ID", "ITEM_PUID", "MAX(TIME_CREATED) TIME_CREATED")
						.from("RECENT_HISTORY").where().col("UPPER(USER_NAME)").eq("JOE")
						.groupBy("ITEM_ID", "ITEM_PUID").orderBy("TIME_CREATED DESC").query())
				.select("rh.TYPE", "rh.ACTION", "rh.ITEM_ID", "rh.ITEM_PUID", "rh.USER_NAME", "rh.TIME_CREATED",
						"dt.SCRIPT_NAME", "kw.NAME AS KEYWORD_NAME", "cd.CLASS_NAME", "la.VALUE AS REQ_NAME",
						"dt.GRAPHICAL_REPORT_TYPE", "dt.IS_TRACE_REPORT", "dt.REPORT_TYPE",
						"dt.DESCRIPTION AS SCRIPT_DESCRIPTION", "kw.DESCRIPTION AS KEYWORD_DESCRIPTION",
						"ta.VALUE AS REQ_DESCRIPTION1", "ta.IS_HTML AS IS_HTML1", "ca.VALUE AS REQ_DESCRIPTION2",
						"ca.IS_HTML AS IS_HTML2")
				.from("RECENT_HISTORY rh").leftJoin("DOCTOOL_SCRIPTS dt").on().col("rh.TYPE", 1).and().col("rh.ITEM_ID")
				.eq().col("dt.SCRIPT_ID").leftJoin("KEYWORDS kw").on().col("rh.TYPE").in(3, 4, 5, 6).and()
				.col("rh.ITEM_ID").eq().col("kw.KEYWORD_ID").leftJoin("OBJECT1 o1").on().col("rh.TYPE").eq(2).and()
				.col("rh.ITEM_ID").eq().col("o1.CLASS_ID").and().col("rh.ITEM_PUID").eq().col("o1.PUID").and()
				.col("o1.STATUS").eq("Current").leftJoin("CLASS_DEFINITION cd").on()
				.group(new Condition().col("rh.TYPE").eq(2).and().col("rh.ITEM_ID").eq().col("cd.CLASS_ID")).or()
				.group(new Condition().col("rh.TYPE").eq(1).and().col("dt.CLASS_ID").eq().col("cd.CLASS_ID"))
				.leftJoin("LINE_ATTRIBUTE1 la").on().col("rh.TYPE").eq(2).and().col("la.ATTRIBUTE_ID").eq()
				.col("cd.DEFAULT_TITLE").and().col("la.OBJECT_ID").eq().col("o1.OBJECT_ID").and().col("la.CLASS_ID")
				.eq().col("rh.ITEM_ID").leftJoin("TEXT_ATTRIBUTE1 ta").on().col("rh.TYPE").eq(2).and()
				.col("ta.ATTRIBUTE_ID").eq().col("cd.DEFAULT_DESCRIPTION").and().col("ta.OBJECT_ID").eq()
				.col("o1.OBJECT_ID").and().col("ta.CLASS_ID").eq().col("rh.ITEM_ID").leftJoin("CORE_TEXT_ATTRIBUTE1 ca")
				.on().col("rh.TYPE").eq(2).and().col("ca.ATTRIBUTE_ID").eq().col("cd.DEFAULT_DESCRIPTION").and()
				.col("ca.OBJECT_ID").eq().col("o1.OBJECT_ID").and().col("ca.CLASS_ID").eq().col("rh.ITEM_ID")
				.innerJoin("Recents").on().col("rh.ITEM_ID").eq().col("Recents.ITEM_ID").and().col("rh.TIME_CREATED")
				.eq().col("Recents.TIME_CREATED").and()
				.group(new Condition().col("rh.ITEM_PUID").eq().col("Recents.ITEM_PUID").or()
						.group(new Condition().col("rh.ITEM_PUID").isNull().and().col("Recents.ITEM_PUID").isNull()))
				.where().col("rh.TYPE").in(1, 2, 3, 4, 5, 6).and().col("UPPER(rh.USER_NAME)").eq("JOE")
				.orderBy("rh.TIME_CREATED DESC").query().preparedString());

		System.out.println();
		System.out.println(new Delete("USER_OPTIONS").where().col("USER_ID").eq(9).query()
				.preparedString(new QueryOptions().pretty(false)));

		System.out.println();
		System.out.println(new Delete("USER_OPTIONS").where().col("USER_ID").eq(9).query().preparedString());

		System.out.println();
		System.out.println(new Insert("USER_OPTIONS").col("USER_ID").values(9, 10).col("QUALIFIER").values(0, 0)
				.col("VALUE").values("false", "true").query().string(new QueryOptions().pretty(false)));

		System.out.println();
		System.out.println(new Insert("USER_OPTIONS").col("USER_ID").values(9, 10).col("QUALIFIER").values(0, 0)
				.col("VALUE").values("false", "true").query().string());

		System.out.println();
		System.out.println(new Update("USER_OPTIONS").set("VALUE").value("true").set("TIME_MODIFIED").expr("SYSDATE()")
				.where().col("USER_ID").eq(9).and().col("QUALIFIER").eq(0).query()
				.string(new QueryOptions().pretty(false)));

		System.out.println();
		System.out.println(new Update("USER_OPTIONS").set("VALUE").value("true").set("TIME_MODIFIED").expr("SYSDATE()")
				.where().col("USER_ID").eq(9).and().col("QUALIFIER").eq(0).query().string());

		System.out.println();
		System.out.println(new Select().from("USER").where().query().string());
	}
}
