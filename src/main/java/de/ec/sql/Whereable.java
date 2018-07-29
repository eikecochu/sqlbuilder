package de.ec.sql;

public interface Whereable {
	
	Where where();
	
	Where where(ValueHolder values);

}
