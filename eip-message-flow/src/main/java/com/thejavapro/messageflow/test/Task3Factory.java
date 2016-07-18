package com.thejavapro.messageflow.test;

import com.thejavapro.messageflow.interfaces.ITranformTaskFactory;
import com.thejavapro.messageflow.interfaces.ITransformTask;

public class Task3Factory implements ITranformTaskFactory<String, String> {

	public ITransformTask<String, String> create() {
		
		return new Task3();
	}

}
