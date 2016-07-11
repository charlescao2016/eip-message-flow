package com.thejavapro.messageflow;

public class Message<T> {

	private boolean poisonPill = false; 
	private String messageID;
	private String correlationID;
	private MessageSequence sequence;
	
	private T body;
	
	public Message() {
		
	}
}
