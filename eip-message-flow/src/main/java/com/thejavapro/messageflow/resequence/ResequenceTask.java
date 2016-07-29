package com.thejavapro.messageflow.resequence;

import java.util.concurrent.TimeUnit;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.IProcessingTask;

public class ResequenceTask<I> implements IProcessingTask<I, I>{

	private long timeout;
	private TimeUnit unit;
	private int maxBufferSzie;
	
	public ResequenceTask(long timeout, TimeUnit unit, int maxBufferSzie) {
		this.timeout = timeout;
		this.unit = unit;
		this.maxBufferSzie = maxBufferSzie;
	}
	
	public Message<I> doTask(Message<I> t) {
		// TODO Auto-generated method stub
		System.out.println("re-seq" + t.getBody());
		
		return t;
	}

}
