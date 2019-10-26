package de.ec.sql.before;

import de.ec.sql.ValueHolder;
import de.ec.sql.Where;

public interface BeforeWhere {

	Where where();

	Where where(ValueHolder values);

	Where where(Where where);

}
