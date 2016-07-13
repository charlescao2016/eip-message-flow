package com.thejavapro.messageflow.interfaces;

import com.thejavapro.messageflow.Message;

public interface IMessageInput<I> {

	void put(Message<I> message);
}
