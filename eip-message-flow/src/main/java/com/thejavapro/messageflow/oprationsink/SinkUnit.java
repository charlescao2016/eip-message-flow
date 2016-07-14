package com.thejavapro.messageflow.oprationsink;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.interfaces.IMessageInput;
import com.thejavapro.messageflow.interfaces.IMessageOutput;
import com.thejavapro.messageflow.interfaces.IProcessingUnit;

public class SinkUnit<I> implements IProcessingUnit<I, String> {

	private static final Logger LOGGER = Logger.getLogger(SinkUnit.class);

	public IMessageInput<I> getIMessageInput() {
		// TODO Auto-generated method stub
		return null;
	}

	public IMessageOutput<String> getIMessageOutput() {
		
		return null;
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	
}
