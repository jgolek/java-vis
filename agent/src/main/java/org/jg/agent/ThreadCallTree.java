package org.jg.agent;

import java.util.ArrayList;
import java.util.List;

public class ThreadCallTree {

	public final String threadName;

	public final List<CalledMethod> calledMethods = new ArrayList<CalledMethod>();
	
	private CalledMethod lastCalledMethod = null;
	
	private ThreadCallTreeWriter threadCallTreeWriter;

	public ThreadCallTree(String nameArg, ThreadCallTreeWriter threadCallTreeWriterArg) {
		this.threadName = nameArg;
		this.threadCallTreeWriter = threadCallTreeWriterArg;
	}

	public void enterMethod(String calledMethodName) {
		
		CalledMethod calledMethod = new CalledMethod(calledMethodName, lastCalledMethod);
		
		if(lastCalledMethod == null) {
			calledMethods.add(calledMethod);
			lastCalledMethod = calledMethod;
		}else {
			lastCalledMethod.add(calledMethod);
			lastCalledMethod = calledMethod;
		}
		
		threadCallTreeWriter.writeCalledMethod(calledMethod);
	}

	public CalledMethod leaveMethod(String calledMethodName) {
		
		boolean calledMethodIsNotLastCalledMethod = !calledMethodName.equals(lastCalledMethod.name);
		//if(calledMethodIsNotLastCalledMethod) {
		//	throw new RuntimeException("Called method isn't last called method:" + calledMethodName 
		//			+ "  lastMethod: " + lastCalledMethod.name + "  Thread: " + Thread.currentThread().getName());
		//};
		while(calledMethodIsNotLastCalledMethod) {
			lastCalledMethod = lastCalledMethod.parentMethod;		
			threadCallTreeWriter.leaveCalledMethod();
			calledMethodIsNotLastCalledMethod = !calledMethodName.equals(lastCalledMethod.name);
		};
		
		
		lastCalledMethod = lastCalledMethod.parentMethod;		
		threadCallTreeWriter.leaveCalledMethod();

		return lastCalledMethod;
	}
}
