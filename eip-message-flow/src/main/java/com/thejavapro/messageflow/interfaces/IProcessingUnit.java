package com.thejavapro.messageflow.interfaces;

public interface IProcessingUnit<I, O> {

	IMessageInput<I> getIMessageInput();
	IMessageOutput<O> getIMessageOutput();
	
	void start();
	void stop();
}
