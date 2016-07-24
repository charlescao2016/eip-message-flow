package com.thejavapro.messageflow.interfaces;

import com.thejavapro.messageflow.Message;

public interface IProcessingTask<I, O> {

	Message<O> doTask(Message<I> t);
}
