package com.thejavapro.messageflow.resequence;

import java.util.concurrent.TimeUnit;

import com.thejavapro.messageflow.interfaces.IProcessingTask;
import com.thejavapro.messageflow.interfaces.IProcessingTaskFactory;

public class ResequenceTaskFactory<I> implements IProcessingTaskFactory<I, I> {

	private long timeout;
	private TimeUnit unit;
	private int maxBufferSzie;
	
	public ResequenceTaskFactory(long timeout, TimeUnit unit, int maxBufferSzie) {
		this.timeout = timeout;
		this.unit = unit;
		this.maxBufferSzie = maxBufferSzie;
	}
	
	public IProcessingTask<I, I> create() {

		return new ResequenceTask<I>(timeout, unit, maxBufferSzie);
	}

}
