package com.thejavapro.messageflow.interfaces;

import com.thejavapro.messageflow.Message;

public interface IOperationTask<I> {

	void doTask(Message<I> t);
}
