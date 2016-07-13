package com.thejavapro.messageflow;

import org.apache.log4j.Logger;

public class Message<T> {

	private static final Logger LOGGER = Logger.getLogger(Message.class);
	
	private boolean poisonPill = false; 
	private String messageID;
	private String correlationID = null;
	private MessageSequence sequence = null;
	
	private T body;
	
	public Message(String messageID, T body) {
		this.messageID = messageID;
		this.body = body;
	}
}
