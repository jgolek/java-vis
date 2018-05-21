package org.jg.agent;

import java.util.ArrayList;
import java.util.List;

public class ThreadCallTree {

	public final String threadName;

	public final List<CalledMethod> calledMethods = new ArrayList<CalledMethod>();
	
	private CalledMethod lastCalledMethod = null;
	
	public ThreadCallTree(String nameArg) {
		this.threadName = nameArg;
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
		
	}

	public void leaveMethod() {
		
		lastCalledMethod = lastCalledMethod.parentMethod;		
	}
}
