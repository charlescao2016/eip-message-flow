package com.thejavapro.messageflow.interfaces;

public interface ITranformTaskFactory<I, O> {

	ITransformTask<I, O> create();
}
