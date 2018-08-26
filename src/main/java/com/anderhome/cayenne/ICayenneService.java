package com.anderhome.cayenne;

import org.apache.cayenne.ObjectContext;

public interface ICayenneService {

	ObjectContext sharedContext();
	ObjectContext newObjectContext();

}
