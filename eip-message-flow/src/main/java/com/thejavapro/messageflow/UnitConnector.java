package com.thejavapro.messageflow;

import java.util.concurrent.BlockingQueue;

import com.thejavapro.messageflow.interfaces.IProcessingUnit;

public class UnitConnector<T> {

	private BlockingQueue<Message<T>> outputQueue = null;
	private IProcessingUnit<T, ?> outputUnit = null;
	
	public BlockingQueue<Message<T>> getOutputQueue() {
		return outputQueue;
	}
	
	public void setOutputQueue(BlockingQueue<Message<T>> outputQueue) {
		this.outputQueue = outputQueue;
		this.outputUnit = null;
	}
	
	public IProcessingUnit<T, ?> getOutputUnit() {
		return outputUnit;
	}
	
	public void setOutputUnit(IProcessingUnit<T, ?> outputUnit) {
		this.outputUnit = outputUnit;
		this.outputQueue = null;
	}
	
	public void put(Message<T> message) throws InterruptedException {
		if (outputUnit != null) {
			outputUnit.put(message);
			return;
		} 
		
		if (outputQueue != null) {
			outputQueue.put(message);
			return;
		}
	}
}
