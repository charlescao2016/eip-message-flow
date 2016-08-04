package com.thejavapro.messageflow.resequence;

import java.util.Comparator;

import com.thejavapro.messageflow.Message;

class MessageComparator<T> implements Comparator<Message<T>> {

	@Override
	public int compare(Message<T> left, Message<T> right) {
		
		if (left.getSequenceNumber() < right.getSequenceNumber()) {
			return -1;
		}
		
		if (left.getSequenceNumber() > right.getSequenceNumber()) {
			return 1;
		} 
		
		return 0;
	}

}
