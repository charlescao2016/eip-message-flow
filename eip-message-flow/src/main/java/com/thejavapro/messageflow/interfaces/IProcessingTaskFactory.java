package com.thejavapro.messageflow.interfaces;

public interface IProcessingTaskFactory<I, O> {

	IProcessingTask<I, O> create();
}
