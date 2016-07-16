package com.thejavapro.messageflow.test;

import com.thejavapro.messageflow.interfaces.IOperationTask;
import com.thejavapro.messageflow.interfaces.IOperationTaskFactory;

public class Task2Factory implements IOperationTaskFactory<String> {

	public IOperationTask<String> create() {
		
		return new Task2();
	}

}
