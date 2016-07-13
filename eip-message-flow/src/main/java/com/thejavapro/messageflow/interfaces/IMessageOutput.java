package com.thejavapro.messageflow.interfaces;

import com.thejavapro.messageflow.Message;

public interface IMessageOutput<O> {

	Message<O> take();
}
