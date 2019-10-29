# SQLDeveloper

Java builder to create SQL statements

The following keywords are supported: DELETE, FROM, GROUP BY, HAVING, INSERT, JOIN, ORDER BY, SELECT, UPDATE, WHERE, WITH

The builder does not check if the created statement is correct or can be interpreted by the target database.

## SELECT

```java
String sql = new Select("*")
	.from("USERS")
	.where()
		.col("USER_ID").gt(5)
		.or()
		.col("USER_NAME").like("%test%")
	.orderBy("USER_NAME")
	.string();
```
will result in:
```SQL
SELECT *
  FROM USERS
 WHERE USER_ID > 5 OR USER_NAME LIKE '%test%'
 ORDER BY USER_NAME ASC
```

## INSERT

```java
String sql = new Insert("USERS")
	.column("USER_ID", 1)
	.column("USER_NAME").value("Administrator")
	.string();
```
will result in:
```SQL
INSERT INTO USERS (USER_ID, USER_NAME)
VALUES (1, 'Administrator')
```

## UPDATE

```java
String sql = new Update("USERS")
	.set("USER_NAME", "UpdatedAdmin")
	.where()
	.col("USER_ID", 1)
	.string();
```
will result in
```SQL
UPDATE USERS
   SET USER_NAME = 'UpdatedAdmin'
 WHERE USER_ID = 1
```

## DELETE

```java
String sql = new Delete("USERS")
	.where()
		.col("USER_ID").eq(1)
		.or()
		.col("USER_NAME").not().eq("Administrator")
	.string();
```
will result in
```SQL
DELETE FROM USERS
 WHERE USER_ID = 1 OR NOT USER_NAME = 'Administrator'
```

## Prepared statements

The above commands all create SQL strings. If a PreparedStatement should be used instead, the builder will handle the creation of the sql string with wildcards, and insert the passed values in the correct order into the created PreparedStatement:

```java
Connection connection = ...
PreparedStatement stmt = new Select("*")
	.from("USERS")
	.where()
		.col("USER_ID").gt(5)
		.or()
		.col("USER_NAME").like("%test%")
	.orderBy("USER_NAME")
	.query()
	.prepare(connection);
ResultSet resultSet = stmt.executeQuery();
```
will execute:
```SQL
SELECT *
  FROM USERS
 WHERE USER_ID > ? OR USER_NAME LIKE ?
 ORDER BY USER_NAME ASC
```
and the parameters:
```java
[5, "%test"]
```

## More examples

For more examples, refer to the Tests class in the test directory