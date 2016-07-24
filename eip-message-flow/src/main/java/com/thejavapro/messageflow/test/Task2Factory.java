package com.thejavapro.messageflow.test;

import com.thejavapro.messageflow.interfaces.IProcessingTask;
import com.thejavapro.messageflow.interfaces.IProcessingTaskFactory;

public class Task2Factory implements IProcessingTaskFactory<String, String> {

	public IProcessingTask<String, String> create() {
		return new Task2();
	}

}
