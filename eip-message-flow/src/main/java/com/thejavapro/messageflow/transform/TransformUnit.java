package com.thejavapro.messageflow.transform;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.IMessageInput;
import com.thejavapro.messageflow.interfaces.IMessageOutput;
import com.thejavapro.messageflow.interfaces.IProcessingUnit;

public class TransformUnit<I, O> implements IMessageInput<I>, IMessageOutput<O>, IProcessingUnit {

	public Message<O> take() {
		// TODO Auto-generated method stub
		return null;
	}

	public void put(Message<I> message) {
		// TODO Auto-generated method stub
		
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
