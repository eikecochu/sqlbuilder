package de.ec.sql;

public interface BeforeWhere {

	Where where();

	Where where(ValueHolder values);

	Where where(Where where);

}
