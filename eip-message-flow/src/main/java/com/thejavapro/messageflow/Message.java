package com.thejavapro.messageflow;

import org.apache.log4j.Logger;

public class Message<T> {

	private static final Logger LOGGER = Logger.getLogger(Message.class);
	 
	private String messageID;
	private String correlationID = null;
	private MessageSequence sequence = null;
	private long sequenceNumber = 0;
	
	private T body;
	
	public Message(String messageID, T body, long sequenceNumber) {
		this.messageID = messageID;
		this.body = body;
		this.sequenceNumber = sequenceNumber;
	}

	public Message(String messageID, T body) {
		this.messageID = messageID;
		this.body = body;
	}

	public String getMessageID() {
		return messageID;
	}

	public String getCorrelationID() {
		return correlationID;
	}

	public void setCorrelationID(String correlationID) {
		this.correlationID = correlationID;
	}

	public MessageSequence getSequence() {
		return sequence;
	}

	public void setSequence(MessageSequence sequence) {
		this.sequence = sequence;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

	public long getSequenceNumber() {
		return sequenceNumber;
	}
}
