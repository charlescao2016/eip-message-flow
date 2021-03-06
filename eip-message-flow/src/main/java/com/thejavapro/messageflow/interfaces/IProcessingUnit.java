package com.thejavapro.messageflow.interfaces;

import java.util.concurrent.BlockingQueue;

import com.thejavapro.messageflow.Message;

public interface IProcessingUnit<I, O> {
	
	void put(Message<I> message) throws InterruptedException;
	void addOutputQueue(BlockingQueue<Message<O>> outputQueue);
	IProcessingUnit<O, ?> addOutputUnit(IProcessingUnit<O, ?> next);
	ITaskManager getITaskManager();
}
