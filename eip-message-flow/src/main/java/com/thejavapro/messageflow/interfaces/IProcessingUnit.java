package com.thejavapro.messageflow.interfaces;

import java.util.concurrent.BlockingQueue;

import com.thejavapro.messageflow.Message;

public interface IProcessingUnit<I, O> extends ITaskManager {
	
	void put(Message<I> message) throws InterruptedException;
	void addOutputQueue(BlockingQueue<Message<O>> outputQueue);
	void addOutputUnit(IProcessingUnit<O, ?> next);
	BlockingQueue<Message<I>> getInputQueue();
}
