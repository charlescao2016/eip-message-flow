package com.thejavapro.messageflow.interfaces;

public interface IPoisonPillEvent {

	void addConsumerThread(Thread thread);
	void interruptOtherConsumers(Thread callingThread);
}
