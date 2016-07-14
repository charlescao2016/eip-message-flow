package com.thejavapro.messageflow.transform;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.interfaces.IMessageInput;
import com.thejavapro.messageflow.interfaces.IMessageOutput;
import com.thejavapro.messageflow.interfaces.IProcessingUnit;

public class TransformUnit<I, O> implements IProcessingUnit<I, O> {

	private static final Logger LOGGER = Logger.getLogger(TransformUnit.class);

	public IMessageInput<I> getIMessageInput() {
		// TODO Auto-generated method stub
		return null;
	}

	public IMessageOutput<O> getIMessageOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
}
