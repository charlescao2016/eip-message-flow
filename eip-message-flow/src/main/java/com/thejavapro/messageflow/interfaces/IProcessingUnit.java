package com.thejavapro.messageflow.interfaces;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.thejavapro.messageflow.Message;

public interface IProcessingUnit<I, O> {
	
	void put(Message<I> message) throws InterruptedException;
	Message<O> take() throws InterruptedException;
	void awaitTermination(long timeout, TimeUnit unit, boolean forAll) throws InterruptedException;
	void shutdown(boolean all);
	BlockingQueue<Message<I>> getInputQueue();
}
