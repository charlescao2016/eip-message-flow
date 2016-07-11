package com.thejavapro.messageflow;

public class MessageSequence {

	private String sequenceID;
	private int position;
	private int size = -1;
	private boolean end = false;
	
	public MessageSequence() {
		
	}
	
	public MessageSequence(String sequenceID, int position) {
		this.sequenceID = sequenceID;
		this.position = position;
	}
	
	public String getSequenceID() {
		return sequenceID;
	}
	public void setSequenceID(String sequenceID) {
		this.sequenceID = sequenceID;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public boolean isEnd() {
		return end;
	}
	public void setEnd(boolean end) {
		this.end = end;
	}
	
}
