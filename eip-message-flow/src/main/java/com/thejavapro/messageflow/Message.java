package com.thejavapro.messageflow;

import org.apache.log4j.Logger;

public class Message<T> {

	private static final Logger LOGGER = Logger.getLogger(Message.class);
	 
	private String messageID;
	private String correlationID = null;
	private MessageSequence sequence = null;
	private long sequenceNumber = 0;
	private boolean isPoisonPill = false;
	
	private T body;
	
	public static <E> Message<E> CreatePoisonPill() {
		return new Message<E>("", null, Long.MAX_VALUE, true);
	}
	
	public Message(String messageID, T body, long sequenceNumber, boolean isPoisonPill) {
		this.messageID = messageID;
		this.body = body;
		this.sequenceNumber = sequenceNumber;
		this.isPoisonPill = isPoisonPill;
	}

	public Message(String messageID, T body, long sequenceNumber) {
		this(messageID, body, sequenceNumber, false);
	}

	public Message(String messageID, T body) {
		this(messageID, body, 0, false);
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

	public boolean isPoisonPill() {
		return isPoisonPill;
	}
}
