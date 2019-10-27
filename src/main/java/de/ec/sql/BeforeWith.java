package de.ec.sql;

public interface BeforeWith {

	With with(String name);

	With with(With with);

}
